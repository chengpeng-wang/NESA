import java.util.Scanner;
public class Main {
public static void main(String[] args) {
Scanner scanner = new Scanner(System.in);
int a = scanner.nextInt();
int[] b = new int[a];
int c = Integer.MAX_VALUE;
int d = Integer.MIN_VALUE;
long e = 0;
for (int i = 0; i < a; i++) {
int f = scanner.nextInt();
c = Math.min(c, f);
d = Math.max(d, f);
e += f;
}
System.out.println(String.format("%d %d %d", c, d, e));
}
}