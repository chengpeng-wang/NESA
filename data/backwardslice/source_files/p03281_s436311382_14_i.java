import java.util.Scanner;
class Main {
public static void main(String[] args) {
Scanner stdIn = new Scanner(System.in);
int n = stdIn.nextInt();
byte divisor;
byte div8 = 0;
if (n % 2 == 0) {
n--;
}
for (; n > 0; n -=2) {
divisor = 0;
for (int i = 1; i <= n; i++ ) {
if (n % i == 0) {
divisor++;
}
}
if (divisor == 8) {
div8++;
}
}
System.out.println(div8);
}
}