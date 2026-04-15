import java.util.Scanner;
public class Main {
static final int MOD = 1000000007;
public static void main(String[] args) {
Scanner sc = new Scanner(System.in);
int N = Integer.parseInt(sc.next());
int M = Integer.parseInt(sc.next());
boolean[] a = new boolean[N + 1];
for (int i = 0; i < M; i++) {
a[Integer.parseInt(sc.next())] = true;
}
sc.close();
long[] ans = new long[N + 1];
ans[0] = 1;
ans[1] = a[1] ? 0 : 1;
for (int i = 2; i <= N; i++) {
if (a[i]) {
ans[i] = 0;
} else {
ans[i] = getMod(ans[i - 1] + ans[i - 2]);
}
}
System.out.println(getMod(ans[N]));
}
static long getMod(long a) {
return a >= 0 ? a % MOD : MOD + a % MOD;
}
}