import java.util.*;
public class Main {
public static void main(String[] args) {
Scanner sc = new Scanner(System.in);
String S = sc.next();
int L = S.length();
StringBuilder buf = new StringBuilder();
for (int i = 0;i<L;i++) {
buf.append("x");
}
String res = buf.toString();
System.out.println(res);
}
}