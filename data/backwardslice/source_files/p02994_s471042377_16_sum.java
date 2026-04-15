import java.io.PrintWriter;
import java.util.*;
public class Main {
static String br = System.getProperty("line.separator");
static Scanner sc = new Scanner(System.in);
static PrintWriter out = new PrintWriter(System.out);
public static void main(String[] args) {
int N = nextInt();
int L = nextInt();
int sum = 0;
int min = 0;
for(int i=1; i<=N; i++){
int num = L+i-1;
if(i == 1) min = num;
min = Math.abs(min) > Math.abs(num) ? num : min;
sum += num;
}
println(sum - min);
}
static int nextInt() {
return Integer.parseInt(next());
}
static Long nextLong() {
return Long.parseLong(next());
}
static String next() {
return sc.next();
}
static String nextLine(){
return sc.nextLine();
}
static void println(Object o) {
out.println(o);
out.flush();
}
static void print(Object o) {
PrintWriter out = new PrintWriter(System.out);
out.print(o);
out.flush();
}
}