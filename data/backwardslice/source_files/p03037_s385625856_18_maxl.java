import java.util.Scanner;
public class Main {
static Scanner scanner;
public static void main(String[] args) {
scanner = new Scanner(System.in);
int N= gi();
int M= gi();
int[] L = new int[M];
int[] R = new int[M];
for (int i=0; i<M;i++) {
L[i]=gi();
R[i]=gi();
}
int minr=Integer.MAX_VALUE;
int maxl=0;
for (int i=0; i<M;i++) {
if(maxl < L[i]) {
maxl=L[i];
}
if(minr>R[i]) {
minr=R[i];
}
}
System.out.print(Math.max(minr-maxl+1,0));
}
// 文字列として入力を取得
public static String gs() {
return scanner.next();
}
// intとして入力を取得
public static int gi() {
return Integer.parseInt(scanner.next());
}
// longとして入力を取得
public static long gl() {
return Long.parseLong(scanner.next());
}
}