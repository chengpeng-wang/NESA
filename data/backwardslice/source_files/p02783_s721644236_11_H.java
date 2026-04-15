import java.util.Scanner;
//AtCoder Beginner Contest 153
//A - Serval vs Monster
public class Main {
public static void main(String[] args) {
Scanner sc = new Scanner(System.in);
int H = Integer.parseInt(sc.next());
int A = Integer.parseInt(sc.next());
sc.close();
long ans = (H / A);
if (H % A != 0) {
ans++;
}
System.out.println(ans);
}
}