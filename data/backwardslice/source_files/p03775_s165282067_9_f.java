import java.util.Scanner;
public class Main {
public static void main(String[] args) {
Scanner sc = new Scanner(System.in);
long n = sc.nextLong();
long ans = 1L << 60;
for (long i = 1; i * i <= n; i++) {
if (n % i == 0) {
int f = Math.max(("" + i).length(), ("" + (n / i)).length());
ans = Math.min(ans, f);
}
}
System.out.println(ans);
}
}