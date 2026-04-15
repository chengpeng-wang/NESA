import java.util.Scanner;
class Main {
public static void main(String[] args) {
Scanner sc = new Scanner(System.in);
int a = sc.nextInt();
int b = sc.nextInt();
int c = sc.nextInt();
int x = sc.nextInt();
int ans = 0;
for (int i = 0; i <= a; i++) {
for (int j = 0; j <= b; j++) {
int charge = x - i * 500 - j * 100;
if (charge / 50 <= c && charge >= 0)
ans++;
}
}
System.out.println(ans);
}
}