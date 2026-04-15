import java.util.Scanner;
public class Main{
public static void main(String[] args){
Scanner sc = new Scanner(System.in);
final int K = sc.nextInt();
final String S = sc.next();
sc.close();
String ans;
int len = S.length();
if(K<len) {
ans = S.substring(0, K) + "...";
}else {
ans = S;
}
System.out.println(ans);
}
}