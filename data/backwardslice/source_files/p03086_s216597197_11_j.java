import java.util.Scanner;
public class Main {
public static void main(String[] args) throws Exception {
Scanner scanner = new Scanner(System.in);
String a = scanner.next();
int n = a.length();
char[] b = a.toCharArray();
scanner.close();
int ans = 0;
for (int i = 0; i < n; i++) {
for (int j = i; j < n; j++) {
int k = solve(b,i,j);
if (ans < k) ans = k;
}
}
System.out.println(ans);
}
private static int solve(char[] a, int i, int j) {
for (int k = i; k <= j; k++) {
if (a[k] == 'A' || a[k] == 'G' || a[k] == 'C' || a[k] == 'T') continue;
return 0;
}
return j - i + 1;
}
}