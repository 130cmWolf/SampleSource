PasswordTextBox  
============

Android/iOSのようなパスワード用のテキストボックスを実現します。  
(ただしきちんとテストしていないα版)  


## 機能
　最終入力(入力直後)文字を平文表示し、  
　時間が経過するとマスク表示処理を行います。  


## 動作確認
* Windows 7 Professional (32bit)  
  Visual Studio 2012 Professional  
  .NET Framework 4.0  

* Windows 8 Professional (64bit)  
  Visual Studio Express 2013 for Windows Desktop  
  .NET Framework 4.0  

* Windows Enbedded Compact 7 (ARM)  
  Visual Studio 2008 Professional  
  .NET Compact Framework 3.5.1  

で動作確認  
.NETの環境なら動くはず  
WM 8とかは知らないです。  
標準で実装されてるんじゃ？　知らんけど。  

## 制限事項
日本語入力回りの処理が入っていません  
適当です  
多分入れる必要無いと思っています。  

現状では  
WindowsPC上では日本語入力を行っても反映されない様です。  
WindowsEC7上では日本語入力を行った際入力(表示)はされますが、  
PasswordTextには反映されず、ASCII文字等何等かの入力を受けた際にASCII以外部分を除去します。  

という動作してました。  

コピペによる貼り付けは対応してませんでした。
切り取りを行うとPasswordTextが更新されました。
コピーを行うとその部分が削除されます。

## How to Use
プロパティ  
* PasswordChar  
  パスワードマスク文字を指定します。

* Interval  
  最終入力文字をマスク処理するまでの時間  

* PasswordText  
  Textプロパティは利用できません。  
  PasswordTextで取得してください。  
