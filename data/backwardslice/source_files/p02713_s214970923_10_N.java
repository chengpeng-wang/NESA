import java.util.Scanner;
public class Main {
public static void main(String[] args) {
Scanner scan = new Scanner(System.in);
int N = scan.nextInt();
scan.close();
long sum = 0;
for(int a=1; a<=N; a++) {
for(int b=1; b<=N; b++) {
for(int c=1; c<=N; c++) {
sum += gcd(a, gcd(b,c));
}
}
}
System.out.println(sum);
}
public static int gcd(int a, int b) {
int r = a % b;
while(r != 0) {
a = b;
b = r;
r = a % b;
}
return b;
}
}