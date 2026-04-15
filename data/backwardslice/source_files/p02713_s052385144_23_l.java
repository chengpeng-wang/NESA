import java.util.Scanner;
public class Main {
static void log(String s) {
System.out.println(s);
}
static void log(int i) {
System.out.println(i);
}
static void log(long i) {
System.out.println(i);
}
static void log(double d) {
System.out.printf("%.12f\n",d);
}
public static void main(String[] args) {
Scanner sc = new Scanner(System.in);
int k = sc.nextInt();
long total=0;
int gcd1;
for(int i=1; i<=k; i++) {
for(int j=1; j<=k; j++) {
gcd1 = gcd2(i,j);
for(int l=1 ;l<=k; l++) {
total += gcd2(gcd1,l);
}
}
}
sc.close();
log( total );
}
public static int gcd2(int m, int n) {
int r;
while (n > 0) {
r = m % n;
m = n;
n = r;
}
return m;
}
}