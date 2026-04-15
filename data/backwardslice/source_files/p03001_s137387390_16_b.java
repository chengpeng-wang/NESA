import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.StringTokenizer;
public class Main {
private static final int MOD = (int)Math.pow(10, 9);
public static void main(String[] args) {
FastReader sc = new FastReader();
int a = sc.nextInt();
int b = sc.nextInt();
int x = sc.nextInt();
int y = sc.nextInt();
double minMax = ((double)a * (double)b / 2.0);
int res = 0;
if ((x * 2) == a && (y * 2) == b) {
res = 1;
}
System.out.println(minMax + " " + res);
}
static class FastReader {
BufferedReader br;
StringTokenizer st;
public FastReader() {
br = new BufferedReader(new InputStreamReader(System.in));
}
String next() { 
while (st == null || !st.hasMoreElements()) {
try {
st = new StringTokenizer(br.readLine());
} catch (IOException  e) {
e.printStackTrace();
}
}
return st.nextToken();
}
int nextInt() {
return Integer.parseInt(next());
}
long nextLong() {
return Long.parseLong(next());
}
double nextDouble() { 
return Double.parseDouble(next());
}
String nextLine() {
String str = "";
try{
str = br.readLine();
} catch (IOException e) {
e.printStackTrace();
}
return str;
}
}
}