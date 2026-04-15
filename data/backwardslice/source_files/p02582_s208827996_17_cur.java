import java.io.*;
import java.math.*;
import java.util.*;
public class Main {
static final long MOD = 1000000007L;
public static void main(String[] args) {
FastScanner sc = new FastScanner();
PrintWriter pw = new PrintWriter(System.out);
String s = sc.next();
int ans = 0;
int cur = 0;
for (int i = 0; i < s.length(); i++) {
if (s.charAt(i)=='R') {
cur++;
} else {
ans = Math.max(ans, cur);
cur = 0;
}
}
ans = Math.max(ans, cur);
pw.println(ans);
pw.close();
}
static class FastScanner { 
BufferedReader br; 
StringTokenizer st; 
public FastScanner() { 
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
int ni() { 
return Integer.parseInt(next()); 
}
long nl() { 
return Long.parseLong(next()); 
} 
double nd() { 
return Double.parseDouble(next()); 
} 
String nextLine() {
String str = ""; 
try { 
str = br.readLine(); 
} catch (IOException e) {
e.printStackTrace(); 
} 
return str;
}
}
}