import java.util.Scanner;
public class Main {
public static void main(String[] args) {
Scanner input = new Scanner(System.in);
long N = input.nextLong();
long hi = N;
long lo = 1;
for (long i = 2; i <= Math.sqrt(N); i++) {
if (N%i==0) {
lo = i;
hi = N/i;
}
}
System.out.println(lo+hi-2);
}
}