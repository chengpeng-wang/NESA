import java.util.Arrays;
import java.util.Scanner;
public class Main {
public static void main(String[] args) {
Scanner sc = new Scanner(System.in);
int n = sc.nextInt();
sc.next();
String[] s = new String[n];
for (int i = 0; i < s.length; i++) {
s[i] = sc.next();
}
Arrays.sort(s);
for (String string : s) {
System.out.print(string);
}
System.out.println();
sc.close();
}
}