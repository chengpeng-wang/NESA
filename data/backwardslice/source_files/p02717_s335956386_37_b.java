import java.io.*;
import java.util.ArrayList;
import java.util.StringTokenizer;
import static java.lang.Math.max;
public class Main {
static void run() throws IOException {
file("", "");
solution();
end();
}
static void solution() throws IOException {
int a = nextInt();
int b = nextInt();
int c = nextInt();
out.println(c+" "+a+" "+b);
}
static mem create_mem(int u, int v) {
mem y = new mem();
y.u = u;
y.v = v;
return y;
}
static class t {
long value;
int index;
}
static class mem {
int u, v;
}
static long pow(long a, long b) {
long h = 1;
while (b > 0) {
if (b % 2 != 0) {
h *= a;
b--;
}
b /= 2;
a *= a;
}
return h;
}
static long lcm(long a, long b) {
return a * b / gcd(a, b);
}
static long gcd(long a, long b) {
return b == 0 ? a : gcd(b, a % b);
}
static void end() {
out.flush();
out.close();
}
static BufferedReader br;
static PrintWriter out;
static void file(String input, String output) {
if (input.equals("")) {
br = new BufferedReader(new InputStreamReader(System.in));
out = new PrintWriter(System.out);
} else {
try {
br = new BufferedReader(new FileReader(input));
} catch (FileNotFoundException e) {
e.printStackTrace();
}
try {
out = new PrintWriter(output);
} catch (FileNotFoundException e) {
e.printStackTrace();
}
}
}
static StringTokenizer in = new StringTokenizer("");
public static String nextToken() throws IOException {
while (in == null || !in.hasMoreTokens()) {
String s = br.readLine();
if (s == null) return null;
in = new StringTokenizer(s);
}
return in.nextToken();
}
public static int nextInt() throws IOException {
return Integer.parseInt(nextToken());
}
public static double nextDouble() throws IOException {
return Double.parseDouble(nextToken());
}
public static long nextLong() throws IOException {
return Long.parseLong(nextToken());
}
public static void main(String[] args) throws IOException {
run();
}
}