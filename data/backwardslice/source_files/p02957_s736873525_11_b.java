import java.util.Scanner;
public class Main {
public static void main(String[] args) {
Scanner sc = new Scanner(System.in);
int a = sc.nextInt();
int b = sc.nextInt();
if (Math.abs(a - b) % 2 != 0) {
System.out.println("IMPOSSIBLE");
} else {
int tmp = Math.abs(a - b) / 2;
if (a < b) {
System.out.println(a + tmp);
} else {
System.out.println(b + tmp);
}
}
}
}