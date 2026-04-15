import java.util.Scanner;
public class Main {
public static void main(String[] args) {
Scanner scanner = new Scanner(System.in);
long N = scanner.nextLong(); scanner.nextLine();
String S = scanner.nextLine();
long ans = 0;
char prev = ',';
for(int i = 0; i < S.length(); i++) {
char cur = S.charAt(i);
if(prev != cur) ans++;
prev = cur;
}
System.out.println(ans);
}
}