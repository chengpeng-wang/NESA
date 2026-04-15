import java.util.*;
public class Main {
public static void main(String[] args) {
Scanner sc = new Scanner(System.in);
int N = sc.nextInt();
int X = sc.nextInt();
int ans = 0;
int minM = Integer.MAX_VALUE;
for (int i = 0; i < N; i++) {
int m = sc.nextInt();
X -= m;
ans++;
if (m < minM) {
minM = m;
}
}
ans += X/minM;
System.out.println(ans);
}
}