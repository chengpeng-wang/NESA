import java.util.*;
public class Main {
public static void main(String[] args) {
Scanner sc = new Scanner(System.in);
long n = sc.nextLong();
long min = 100;
for(long i = 1; i * i <= n; i++) {
if(n % i == 0) {
long devider = n / i;
long maxDigits = countMaxDigits(i, devider);
min = Math.min(min, maxDigits);
}
}
System.out.println(min);
}
static long countMaxDigits(long dev1, long dev2) {
long count = 0;
while(dev1 != 0 || dev2 != 0) {
count++;
dev1 /= 10;
dev2 /= 10;
}
return count;
}
}