import java.util.Scanner;
public class Main {
public static void main(String[] args) throws Exception {
Scanner sc = new Scanner(System.in);
int a = sc.nextInt();
int b = sc.nextInt();
int c = sc.nextInt();
int divisorCount = 0;
for(int i = a; i <= b; i++){
if(c % i == 0) divisorCount++;
}
System.out.println(divisorCount);
}
}
/*覚えよう！！
今回は無し
*/
/*前回の内容！！
〇import java.util.Arrays;は重要↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
・Arrays：ソート（並べ変え）や検索など、配列を操作するための機能
・並べ変えするとき
import java.util.Arrays;を上に書く
class Main内に Arrays.sort(a); と書く　←　このとき()内は配列の変数名
〇配列とは、同じ変数名に複数の値を入力できる箱
※イメージ
前回まで、a = 1, b = 2, c = 3など各変数名は1箱に値を代入できない
今回の配列はa[0] = 1, a[1] = 2, a[2] = 3 のように変数名aは3つの箱に値を代入できる。
注意：配列の箱は｢ 0 ｣から開始する
・複数のif(条件)：かつ｢ && ｣とまたは｢ || ｣の２つを使用する
※かつ｢ && ｣：「真 && 真 = 真」, 「真 && 偽 = 偽」, 「偽 && 真 = 偽」 ,「偽 && 偽 = 偽」の4つの条件
または｢ || ｣：「真 && 真 = 真」, 「真 && 偽 = 真」, 「偽 && 真 = 真」 ,「偽 && 偽 = 偽」の4つの条件
・関係を比較する：if(条件式)
例：if(2 < 3) この条件は｢真（成立）｣　→　何か命令があれば実行する
if(5 < 3) この条件は｢偽（不成立）｣　→　何か命令があれば実行しない
・比較の条件式：等しいか比較｢==｣、等しくないか比較｢!=｣ 
大きいか比較｢<｣、小さいか比較｢>｣
以上の比較｢<=｣、以下の比較｢>=｣
・else；ifの条件以外の場合に使用する。
例：if(雨 == 降っている) System.out.println("傘を持って行く");
else System.out.println("傘を持って行かない");
・else if：詳細な場合分けしたいときに使用
例：if(雨 == 降っている) System.out.println("傘を持って行く");
else if(雨 == 午後から降るかも) System.out.println("念のため傘を持って行く");
else System.out.println("傘を持って行かない");
・変数に計算結果を代入する：面積　＝　縦　×　横
※計算は変数名でも数値でもいける
・四則演算：足し算｢＋｣、引き算｢－｣、掛け算｢＊｣、割り算｢／｣
1 + 2 = 3;
4 - 3 = 1;
2 * 5 = 10;
8 / 2 = 4;
・余りの計算：割り算の余り｢％｣
5 % 3 = 2;
・printlnの複数表示：｢＋｣を使用
例：println("Kobe" + "Kagikou"); = KobeKajikou
println("明治 + 20 + "年"); = 明治20年　数値が入ってもOK！
・変数の宣言は｢型｣と｢変数名｣の２つ必要
例：型＿変数名　→　int x;
・キーボードから入力するときは・・・｢ import java.util.Scanner; ｣ と ｢  Scanner sc = new Scanner(System.in) ; ｣が必要
・入力するとき・・・変数名 = sc.nextInt(); nextの後は型を書く、頭文字は大文字　
※  scは変更しても大丈夫しかし全て変更後の文字に統一　例：nyuryoku
・println・・・画面に出力するときに使用
・文字列の表示・・・"〇〇〇の間に入力"ことが約束
*/