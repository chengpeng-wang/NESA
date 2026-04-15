import java.util.Scanner;
public class Main {
public static void main(String[] args) {
Scanner sc = new Scanner(System.in);
int a = sc.nextInt();
int b = sc.nextInt();
int c = sc.nextInt();
int x = sc.nextInt();
int y = sc.nextInt();
if (a + b < c * 2) {
System.out.println(a * x + b * y);
} else {
int ab = Math.min(x, y);
System.out.println(Math.min((x - ab) * a + (y - ab) * b + c * ab * 2, c * Math.max(x, y) * 2));
}
sc.close();
}
}