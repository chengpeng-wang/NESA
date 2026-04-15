import java.util.Scanner;
/**
* Created by zzt on 17-3-26.
*/
public class Main {
public static void main(String[] args) {
Scanner scanner = new Scanner(System.in);
long n = scanner.nextLong();
int ret = String.valueOf(n).length();
for (long i = 1; i <= n/i ; i++) {
if (n%i==0) {
ret = Math.min(ret, Math.max(String.valueOf(i).length(), String.valueOf(n / i).length()));
}
}
System.out.println(ret);
}
}