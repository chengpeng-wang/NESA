import java.io.PrintWriter;
import java.util.*;
public class Main {
static Scanner scanner;
static PrintWriter out;
public static void main(String[] args) {
scanner = new Scanner(System.in);
out = new PrintWriter(System.out);
int a = nextInt();
int b = nextInt();
int c = nextInt();
if (((double)a / (double)c) > b) {
System.out.println("No");
} else {
System.out.println("Yes");
}
out.flush();
}
public static void println(Object o) {
out.println(o);
}
public static void print(Object o) {
out.print(o);
}
public static void printf(String format, Object... o) {
out.printf(format, o);
}
public static String next() {
return scanner.next();
}
public static int nextInt() {
return Integer.parseInt(scanner.next());
}
public static long nextLong() {
return Long.parseLong(scanner.next());
}
public static double nextDouble() {
return Double.parseDouble(scanner.next());
}
public static short nextShort() {
return Short.parseShort(scanner.next());
}
public static float nextFloat() {
return Float.parseFloat(scanner.next());
}
public static void printYes() {
System.out.println("Yes");
}
public static void printYES() {
System.out.println("YES");
}
public static void printNo() {
System.out.println("No");
}
public static void printNO() {
System.out.println("NO");
}
public static String nextLine() {
return scanner.nextLine();
}
public static double distance(double x, double y, double x2, double y2) {
return Math.sqrt((x2 - x) * (x2 - x) + (y2 - y) * (y2 - y));
}
public static int MOD = 1_000_000_007;
public static int MAX = 100000;
public static long[] fac = new long[MAX];
public static long[] finv = new long[MAX];
public static long[] inv = new long[MAX];
public static void combinit() {
fac[0] = fac[1] = 1;
finv[0] = finv[1] = 1;
inv[1] = 1;
for (int i = 2; i < MAX; i++) {
fac[i] = fac[i - 1] * i % MOD;
inv[i] = MOD - inv[MOD % i] * (MOD / i) % MOD;
finv[i] = finv[i - 1] * inv[i] % MOD;
}
}
public static long combination(int n, int k) {
if (n < k || n < 0 || k < 0) return 0;
return fac[n] * (finv[k] * finv[n - k] % MOD) % MOD;
}
public static long factorial(long n) {
return n <= 0 ? 1 : n * factorial(n - 1);
}
}