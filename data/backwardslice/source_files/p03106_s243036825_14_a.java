import java.util.Scanner;
public class Main {
public static void main(String[] args) {
Scanner sc = new Scanner(System.in);
int A, B, K;
A = sc.nextInt();
B = sc.nextInt();
K = sc.nextInt();
sc.close();
int a = 0;
int ans = 0;
for (int i = 100; i >= 1; i--) {
if (A % i == 0 && B % i == 0) {
a++;
if (a == K) {
ans = i;
}
}
}
System.out.println(ans);
}
}