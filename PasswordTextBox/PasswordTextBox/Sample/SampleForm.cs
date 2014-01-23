using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Windows.Forms;

namespace Sample
{
    public partial class SampleForm : Form
    {
        public SampleForm()
        {
            InitializeComponent();
        }

        private void passwordTextBox1_TextChanged(object sender, EventArgs e)
        {
            label1.Text = passwordTextBox1.PasswordText;
        }
    }
}
