import java.util.Scanner;
public class Main {
public static void main(String[] args) {
Scanner sc = new Scanner(System.in);
int a = sc.nextInt();
int b = sc.nextInt();
int c = sc.nextInt();
String message = "";
if ((a * b * c) == (5 * 7 * 5)) {
message = "YES";
} else {
message = "NO";
}
System.out.println(message);
}
}