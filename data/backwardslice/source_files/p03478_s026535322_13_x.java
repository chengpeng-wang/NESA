import java.util.Scanner;
class Main {
public static void main(String[] args) throws Exception {
Scanner sc = new Scanner(System.in);
int ans = 0;
int n = sc.nextInt();
int a = sc.nextInt();
int b = sc.nextInt();
for (int i = 1; i <= n; i++) {
int x = 0;
int d = i;
while (d > 0) {
x += d % 10;
d /= 10;
}
if (a <= x && x <= b)
ans += i;
}
System.out.println(ans);
sc.close();
}
}