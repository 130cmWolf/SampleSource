using System;
using System.Globalization;
using System.Windows.Forms;

namespace Sample
{
    public partial class Form1 : Form
    {
        public Form1()
        {
            InitializeComponent();
        }

        private void textBox1_TextChanged(object sender, EventArgs e)
        {
            try
            {
                CultureInfo ci = new CultureInfo("ja-JP", false);
                ci.DateTimeFormat.Calendar = new JapaneseCalendar();

                DateTime dt = new DateTime(
                    dateTimePicker1.Value.Year,
                    dateTimePicker1.Value.Month,
                    dateTimePicker1.Value.Day,
                    dateTimePicker2.Value.Hour,
                    dateTimePicker2.Value.Minute,
                    dateTimePicker2.Value.Second
                );
                
                if (checkBox1.Checked)
                    textBox2.Text = dt.ToStringExtension(textBox1.Text, ci);
                else
                    textBox2.Text = dt.ToString(textBox1.Text, ci);
            }
            catch { textBox2.Text = "error"; }
        }
    }
}
