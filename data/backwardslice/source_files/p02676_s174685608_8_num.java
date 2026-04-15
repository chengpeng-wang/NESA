import java.util.Scanner;
public class Main {
public static void main(String[] args) {
Scanner scan = new Scanner(System.in);
int num = scan.nextInt();
String str = scan.next();
scan.close();
if (str.length() <= num) {
System.out.println(str);
} else {
System.out.println(str.substring(0, num) + "...");
}
}
}