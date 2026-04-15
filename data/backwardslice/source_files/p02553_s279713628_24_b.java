import java.util.Scanner;
public class Main {
public static void main(String[] args) {
Scanner sc = new Scanner(System.in);
long a = sc.nextLong();
long b = sc.nextLong();
long c = sc.nextLong();
long d = sc.nextLong();
if (a == 0 && b == 0 || c == 0 && d == 0) {
System.out.println(0);
return;
} else if (a > 0 && d < 0) {
System.out.println(a * d);
return;
} else if (b < 0 && c > 0) {
System.out.println(b * c);
return;
} else if (a == 0 && d < 0 || c == 0 && b < 0) {
System.out.println(0);
return;
} else if (a >= 0 || c >= 0) {
System.out.println(b * d);
return;
} else if (a < 0 && b > 0 && c < 0 && d > 0) {
System.out.println(Math.max(a * c, b * d));
return;
} else {
System.out.println(a * c);
return;
}
}
}