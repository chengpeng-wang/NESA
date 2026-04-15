import java.util.Scanner;
public class Main {
public static void main(String[] args) {
Scanner sc = new Scanner(System.in);
int a = sc.nextInt();
int b = sc.nextInt();
int c = sc.nextInt();
int x = sc.nextInt();
int y = sc.nextInt();
int i = a * x + b * y;
int ii = c * 2 * Math.max(x, y);
int iii = 0;
if (x > y) {
iii = c * 2 * Math.min(x, y) + a * (x - y);
} else {
iii = c * 2 * Math.min(x, y) + b * (y - x);
}
System.out.println(Math.min(Math.min(i, ii), iii));
}
}