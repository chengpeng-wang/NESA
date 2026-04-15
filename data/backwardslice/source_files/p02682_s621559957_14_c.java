import java.util.Scanner;
public class Main {
public static void main(String[] args) {
Scanner sc = new Scanner(System.in);
int a = sc.nextInt();
int b = sc.nextInt();
int c = sc.nextInt();
int k = sc.nextInt();
sc.close();
if ((k - a) <= 0) {
System.out.print(k);
} else if ((k - (a + b)) <= 0) {
System.out.print(a);
} else if ((k - (a + b + c)) <= 0) {
System.out.print(a - (k - a - b));
}
}
}