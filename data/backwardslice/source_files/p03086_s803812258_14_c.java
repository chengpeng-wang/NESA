import java.util.Scanner;
public class Main {
public static void main(String[] args) {
Scanner scan = new Scanner(System.in);
String s = scan.next();
scan.close();
int ans = 0;
for (int i = 0; i < s.length(); i++) {
for (int j = i+1; j <= s.length(); j++) {
String a = s.substring(i, j);
boolean isOK = true;
for (int k = 0; k < a.length(); k++) {
char c = a.charAt(k);
if (c != 'A' && c != 'C' && c != 'G' && c != 'T') {
isOK = false;
break;
}
}
if (isOK) {
ans = Math.max(ans, a.length());
}
}
}
System.out.println(ans);
}
}