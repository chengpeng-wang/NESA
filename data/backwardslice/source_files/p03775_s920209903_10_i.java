import java.util.Scanner;
import java.util.HashSet;
import java.util.Set;
public class Main {
public static void main(String[] args) {
Scanner sc = new Scanner(System.in);
long N = sc.nextLong();
Set<Long> x = new HashSet<>();
for(long i = 1; i * i <= N; i++) {
if(N % i == 0) {
x.add(i);
x.add(N / i);
}
}
int min = 100;
for(Long i : x) {
String a = String.valueOf(i);
String b = String.valueOf(N / i);
int temp = Math.max(a.length(), b.length());
min = Math.min(min, temp);
}
System.out.println(min);
}
}