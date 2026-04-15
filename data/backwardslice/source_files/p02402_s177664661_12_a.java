import java.util.Scanner;
public class Main {
public static void main(String[] args) {
Scanner sc = new Scanner(System.in);
long N = sc.nextLong();
long min = Long.MAX_VALUE;
long max = Long.MIN_VALUE;
long sum = 0;
for (long i = 0; i < N; i++) {
long a = sc.nextLong();
sum += a;
if (min > a) {
min = a;
}
if (max < a) {
max = a;
}
}
System.out.printf("%d %d %d\n", min ,max, sum);
}
}