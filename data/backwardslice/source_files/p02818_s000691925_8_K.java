import java.util.Scanner;
public class Main {
public static void main(String[] args) {
Scanner scanner = new Scanner(System.in);
long A = scanner.nextLong();
long B = scanner.nextLong();
long K = scanner.nextLong();
if (K <= A) {
System.out.printf("%d %d%n", A - K, B);
} else {
B = Math.max(0, B - (K - A));
System.out.printf("%d %d%n", 0, B);
}
}
}