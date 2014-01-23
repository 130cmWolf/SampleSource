PasswordTextBox
Android/iOSのようなパスワード用のテキストボックスを実現します。
============

(ただしきちんとテストしていないα版)


・機能
　最終入力文字列を平文表示し、
　時間が経過するとマスク処理を行います。


・動作
　・Windows 7 Professional (32bit)
　　Visual Studio 2012 Professional
　　.NET Framework 4.0

　・Windows Enbedded Compact 7 (ARM)
　　Visual Studio 2008 Professional
　　.NET Compact Framework 3.5.1

　で動作確認
　.NETの環境なら動くはず


・制限事項
　日本語入力回りの処理が入っていません
　適当です


・How to Use
　プロパティ
　　・PasswordCharEx
　　　パスワードマスク文字を指定します。
　　　PasswordCharは利用しない事。
　　・Interval
　　　最終入力文字をマスク処理するまでの時間
　　・PasswordText
　　　Textプロパティは利用できません。
　　　PasswordTextで取得してください。
