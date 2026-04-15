import java.util.Scanner;
import java.util.stream.IntStream;
public class Main {
public static void main(String[] args) {
final Scanner sc = new Scanner(System.in);
final int N = sc.nextInt();
int sum = 0;
int max = Integer.MIN_VALUE;
for (int i = 0; i < N; i++) {
final int L = sc.nextInt();
max = Math.max(max, L);
sum += L;
}
System.out.println(max*2 < sum ? "Yes" : "No");
}
}