
# Parse [syukujitsu.csv](http://www8.cao.go.jp/chosei/shukujitsu/gaiyou.html) by Scala

Ruby版の "国民の祝日.csv" 解析するプログラムを参考にして、作成したScala版のParaserです。

"国民の祝日.csv"をどう変換するか色々考えた結果、Skinny Frameworkを利用してみました。
JSON形式でクライアントに結果を返す事ができます。

まずはCSVを取り込んで、Scala のListで保持する形となっています。
更にそこからSortedMapへ変換し、JSONでっ返します。

今のところ、トップページ表示の際には、JSONを利用せずに、JavaScriptの変数に結果を配列で設定して、表示する際にはVue.jsを利用しています。

## How to run
ダウンロードして、以下のコマンドで実行可能です。

    ./skinny run

ブラウザ上からは以下のURLでデフォルトではアクセス可能です。

　　http://localhost:8080/

アクセスすると日付順に祝日が表示されます。

## 参考にしたサイト
Qiita [【短命に終わった】国民の祝日.csvをパースして変換するRubyプログラムとコード解説動画](http://qiita.com/jnchito/items/b8a2ed3544c1dc36fb9d)

Qiita [面倒くさいパーサの実装もDSLで書くだけ！そう、Scalaならね](http://qiita.com/suin/items/35bc4afe618cb77f80f6)


## 利用しているフレームワーク
CSVからプログラムで利用する単純な形に変換するだけだと面白くなかったので、現在学習中のSkinny Frameworkを利用してみました。
[Skinny Framework](http://skinny-framework.org/)

クライアントに非表示する際に使用しました。最初にAngular2を考えていましたが、Vue.jsの方が簡易に実装できました。
[Vue.js](https://jp.vuejs.org/)


## License
MIT License
