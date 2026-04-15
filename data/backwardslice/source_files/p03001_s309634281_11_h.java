import java.io.*;
import java.util.*;
public class Main {
public static void main(String[] args) {
InputReader in = new InputReader();
double w = in.nextInt();
double h = in.nextInt();
double x = in.nextInt();
double y = in.nextInt();
int numWays = 0;
if (x == w/2 && y == h/2)
++numWays;
System.out.println(w*h/2 + " " + numWays);
}
static class InputReader {
public BufferedReader br;
public StringTokenizer st;
public InputReader() {
br = new BufferedReader(new InputStreamReader(System.in));
st = null;
}
public String next() {
while (st == null || !st.hasMoreTokens()) {
try {
st = new StringTokenizer(br.readLine());
}
catch (IOException e) {
throw new RuntimeException(e);
}
}
return st.nextToken();
}
public int nextInt() {
return Integer.parseInt(next());
}
}
}