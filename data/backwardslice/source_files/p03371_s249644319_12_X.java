import java.util.Scanner;
public class Main {
public static void main(String[] args) {
final Scanner sc = new Scanner(System.in);
final int A = sc.nextInt();
final int B = sc.nextInt();
final int C = sc.nextInt();
final int X = sc.nextInt();
final int Y = sc.nextInt();
final int price = Math.min(C*2,A+B) * Math.min(X, Y);
final int nokori = Math.max(X, Y) - Math.min(X, Y);
if (X > Y) {
System.out.println(price + Math.min(nokori*A, nokori*C*2));
} else {
System.out.println(price + Math.min(nokori*B, nokori*C*2));
}
}
}