import java.io.*;
import java.util.*;
class Main {
public static void main (String[] args) {
Scanner sc = new Scanner(System.in);
long n = sc.nextLong();
long min = 100000000;
for(long i = 1; i*i <= n; i++) {
if(n % i == 0) {
long j = n/i;
int F = Math.max(Long.toString(i).length(), Long.toString(j).length());
min = Math.min(min, F);
}
}
System.out.println(min);
}
}