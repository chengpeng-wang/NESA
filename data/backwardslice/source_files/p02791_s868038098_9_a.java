import java.util.*;
public class Main {
public static void main(String[] args) {
Scanner sc = new Scanner(System.in);
long n = sc.nextLong();
int count = 1;
int min = sc.nextInt();
for (long i = 0; i < n-1; i++) {
int a = sc.nextInt();
if (a <= min) count++;
min = Math.min(min, a);
}
System.out.println(count);
}
}