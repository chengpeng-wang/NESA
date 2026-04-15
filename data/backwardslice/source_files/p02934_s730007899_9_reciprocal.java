import java.util.Scanner;
public class Main {
public static void main(String[] args) {
Scanner sc = new Scanner(System.in);
int n = getInt(sc);
int[] a = getIntArray(sc);
double reciprocal = 0;
for (int num : a) {
reciprocal += 1.0 / num;
}
System.out.println(1.0 / reciprocal);
}
/**
* *汎用メソッド <br>
* スキャナーからの入力値をlongで返却
*
* @return
*/
public static long getLong(Scanner sc) {
return Long.parseLong(sc.next());
}
/**
* *汎用メソッド <br>
* スキャナーからの入力値をintで返却
*
* @return
*/
public static int getInt(Scanner sc) {
return Integer.parseInt(sc.next());
}
/**
* *汎用メソッド <br>
* スキャナーからの入力値をStringで返却
*
* @return
*/
public static String getString(Scanner sc) {
return sc.next();
}
/**
* *汎用メソッド <br>
* スキャナーからの入力値をint配列で返却
*
* @return
*/
public static int[] getIntArray(Scanner sc) {
String line = "";
// 直前にnext()を実行していると改行コードだけ取ってしまうのでその対策
while (line.equals("")) {
line = sc.nextLine();
}
String[] strArray = line.split(" ");
int[] intArray = new int[strArray.length];
for (int i = 0; i < strArray.length; i++) {
intArray[i] = Integer.parseInt(strArray[i]);
}
return intArray;
}
}