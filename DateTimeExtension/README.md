DateTimeExtension  
============
なんか.NET Frameworkじゃ午前/午後0-11時表記ができないので、  
DateTime用拡張モジュール。  
ついでに和暦を独自に持ち、MSに依存しないように、  
和暦の初期化を触るだけで即時に反映できるようにする。

どの.NET Frameworkでも動くかと  

## ライセンス
Public Domain Software(PDS)  
Fork歓迎  
なんかいい具合に改造してPDSのまま公開してくれるとうれしいなーとか。


## How to Use
メソッド  
* String DateTime.ToStringExtension(String format)  
  標準ToStringのString formatを拡張  
  "b","bb"を追加  
  "i","ii"を追加
  "j"を追加

* String DateTime.ToStringExtension(String format, IFormatProvider provider)  
  IFormatProvider対応  

* int DateTime.GetHour12()  
  0-11時を返します  

* String NowEra(this DateTime dt)  
  和暦名称を取得

* int NowEraYear(this DateTime dt)  
  和暦に対する年を取得  

* public static List<EraData> Eras  
  和暦テーブル  
  Extension内で利用している和暦テーブルで、  
  JapaneseCalendarとは独立している。  
  和暦名称と開始日を新しい順に格納している。

サンプルとしてコード上には新和暦2020/10/10～を追加している。  
利用する場合は削除して使ってもらいたい。

Eras[n] | 和暦     | 開始日
--------|----------|-----------
0       | 仮新和暦 | 2020/10/10～
1       | 平成     | 1989/1/8～
2       | 昭和     | 1926/12/25～
3       | 大正     | 1912/7/30～
4       | 明治     | 1868/9/8～

その他DBシステム上で和暦、日付管理が行われている場合Erasへnewし直し、  
addやnew時にリストを再生成することで対応できる。  
その場合排他に気を付けることが必要である。
時計描画用にTimerを利用している場合にErasを変更してしまうと、  
format "i", "j" が正常に動作しなくなる。

Ex.
```cs
DateTime dt = new DateTime(2014, 4, 4, 12, 00, 00);
dt.ToStringExtension("j i/MM/dd/ tt bb:mm:ss")
```
平成 26/04/04 午後 00:00:00


## メモ
Str.Replace()コストが気になるけどいいや。
