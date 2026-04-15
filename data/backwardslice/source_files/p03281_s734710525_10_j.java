import java.util.Scanner;
public class Main {
public static void main(String[] args) {
final Scanner sc = new Scanner(System.in);
final int N = sc.nextInt();
int ans = 0;
for (int i = 1; i <= N; i++) {
int sum = 0;
for (int j = 1; j <= N; j++) {
if (i % j == 0) {
sum++;
}
}
if (sum == 8 && i % 2 != 0) {
ans++;
}
}
System.out.println(ans);
}
}