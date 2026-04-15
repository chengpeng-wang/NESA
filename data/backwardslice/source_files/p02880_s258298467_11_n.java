import java.math.*;
import java.util.*;
public class Main {
public static void main(String[] args) {
Scanner sc = new Scanner(System.in);
int n = sc.nextInt();
int kuku[][] = new int[9][9];
String ans ="No";
for (int i = 1; i <= 9; i++) {
if (n % i == 0) {
if (n / i <= 9)
ans = "Yes";
}
}
System.out.println(ans);
}
}