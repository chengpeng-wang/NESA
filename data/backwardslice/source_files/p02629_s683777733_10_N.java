import java.util.Scanner;
public class Main {
public static void main(String[] args) {
Scanner sc = new Scanner(System.in);
long N = sc.nextLong();
String tmp = "";
while(N>0) {
--N;
tmp += (char)('a' + (N%26));
N /= 26;
}
StringBuffer sb = new StringBuffer(tmp);
String res = sb.reverse().toString();
System.out.println(res);
}
}