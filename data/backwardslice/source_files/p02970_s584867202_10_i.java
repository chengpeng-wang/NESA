import java.util.Scanner;
public class Main {
public static void main(String[] args) {
Scanner sc = new Scanner(System.in);
int n = sc.nextInt();
int d = sc.nextInt();
int ans = 0;
for (int i = 0; i < n; i++) {
ans++;
i += d*2;
}
System.out.println(ans);
}
}