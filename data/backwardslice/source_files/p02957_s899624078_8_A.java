import java.util.Scanner;
public class Main {
public static void main(String[] args) {
Scanner sc = new Scanner(System.in);
int A = sc.nextInt();
int B = sc.nextInt();
String ans = "";
if (A % 2 != B % 2) {
ans = "IMPOSSIBLE";
} else {
ans = String.valueOf((A + B) / 2);
}
System.out.println(ans);
sc.close();
}
}