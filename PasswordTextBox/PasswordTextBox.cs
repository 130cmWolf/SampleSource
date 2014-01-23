using System;
using System.ComponentModel;
using System.Windows.Forms;

public class PasswordTextBox : TextBox
{
    private Timer MaskTimer = new Timer();
    public PasswordTextBox() :
        base()
    {
        PasswordCharEx = '●';
        MaskTimer.Enabled = false;
        MaskTimer.Tick += new EventHandler(MaskTimer_Tick);
        KeyDown += new KeyEventHandler(PasswordTextBox_KeyDown);
        KeyPress += new KeyPressEventHandler(PasswordTextBox_KeyPress);
    }

    ~PasswordTextBox()
    {
        TimerStop();
    }

#if !WindowsCE
    [Category("表示")]
    [Description("パスワードマスク文字を指定します。\r\nPasswordCharは利用しないでください。")]
#endif
    public char PasswordCharEx { get; set; }

#if !WindowsCE
    [Description("コントロールに関連付けられたテキストです。\r\nパスワード内の文字列はこのプロパティで取得します。")]
#endif
    public String PasswordText { get; set; }

#if !WindowsCE
    [Category("動作")]
    [Description("最後に入力した文字をマスクするまでの時間を設定、取得します。")]
#endif
    public int Interval { get { return MaskTimer.Interval; } set { MaskTimer.Interval = value; } }

    void MaskTimer_Tick(object sender, EventArgs e)
    {
        TimerStop();
        var pos = this.SelectionStart;
        var sellen = this.SelectionLength;
        base.Text = new String(PasswordCharEx, PasswordText.Length);
        this.SelectionStart = pos;
        this.SelectionLength = sellen;
    }

    void PasswordTextBox_KeyPress(object sender, KeyPressEventArgs e)
    {
        if (String.IsNullOrEmpty(PasswordText)) PasswordText = "";
        e.Handled = true;
        TimerStop();
        TimerStart();
        var pos = this.SelectionStart;

        if (pos > PasswordText.Length)
        {
            base.Text = new String(PasswordCharEx, PasswordText.Length);
            return;
        }
        
        // バックスペース処理
        if (e.KeyChar == '\b')
        {
            if (pos == 0 && this.SelectionLength == 0) return;
            if (this.SelectionLength == 0)
            {
                pos--;
                PasswordText = PasswordText.Remove(pos, 1);
            }
            else
            {
                // 範囲指定削除処理
                PasswordText = PasswordText.Remove(pos, this.SelectionLength);
            }
        }
        else
        {
            // 範囲指定入力処理
            if (this.SelectionLength > 0)
            {
                PasswordText = PasswordText.Remove(pos, this.SelectionLength);
            }
        }

        // ASCII文字のみ受け付け
        if (e.KeyChar >= ' ' && e.KeyChar <= '~')
        {
            PasswordText = PasswordText.Insert(pos, new String(e.KeyChar, 1));
            base.Text = new String(PasswordCharEx, PasswordText.Length - 1);
            base.Text = base.Text.Insert(pos, new String(e.KeyChar, 1));
            pos++;
        }
        else
        {
            base.Text = new String(PasswordCharEx, PasswordText.Length);
        }

        this.SelectionStart = pos;
    }

    void PasswordTextBox_KeyDown(object sender, KeyEventArgs e)
    {
        var pos = this.SelectionStart;
        // KeyPressではDeleteが処理できないため代理
        if (e.KeyCode == Keys.Delete)
        {
            if (pos == base.Text.Length) return;
            if (this.SelectionLength == 0)
            {
                PasswordText = PasswordText.Remove(pos, 1);
            }
            else
            {
                // 範囲指定削除処理
                PasswordText = PasswordText.Remove(pos, this.SelectionLength);
            }
        }
    }
    private void TimerStart()
    {
#if !WindowsCE
        MaskTimer.Start();
#else
        MaskTimer.Enabled = true;
#endif
    }

    private void TimerStop()
    {
#if !WindowsCE
        MaskTimer.Stop();
#else
        MaskTimer.Enabled = false;
#endif
    }
}

