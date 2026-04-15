import java.util.Scanner;
public class Main {
public static void main(String[] args) {
Scanner sc = new Scanner(System.in);
long n = sc.nextLong();
String[] list = new String[26];
char tmp = 'a';
for (int i = 0; i < 26; i++) {
list[i] = String.valueOf(tmp++);
}
String ans = "";
while (n > 0) {
n--;
int mod = (int) (n % 26);
ans += list[mod];
n /= 26;
}
StringBuffer str = new StringBuffer(ans);
String s = str.reverse().toString();
System.out.println(s);
}
}