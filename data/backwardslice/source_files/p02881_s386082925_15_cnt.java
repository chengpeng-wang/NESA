import java.util.Scanner;
public class Main {
public static void main(String[] args) {
Scanner stdIn = new Scanner(System.in);
long n = stdIn.nextLong();
long min = n;
long cnt = 1;
for (long i = 1; i < n / cnt; i++) {
if (n % i == 0) {
long a = i + n / i - 2;
if (min > a) {
min = a;
}
} else {
cnt = i;
}
}
System.out.println(min);
}
}