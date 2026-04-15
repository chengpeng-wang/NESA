import java.util.Scanner;
public class Main {
public static void main(String[] args) {
Scanner sc = new Scanner(System.in);
int a = Integer.parseInt(sc.next());
int b = Integer.parseInt(sc.next());
int ans = 0;
if (a == b) {
ans = a * 2;
} else {
ans = Math.max(a, b) + Math.max(a, b) - 1;
}
System.out.println(ans);
}
}