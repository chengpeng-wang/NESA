import java.util.Scanner;
public class Main {
public static void main(String[] args) {
Scanner scanner = new Scanner(System.in);
long n = scanner.nextLong();
long min = Integer.MAX_VALUE;
for(long i=1;i<=Math.sqrt(n);i++){
if(n % i == 0){
long x = n / i;
String y = String.valueOf(i);
String z = String.valueOf(x);
long max = Math.max(y.length(), z.length());
min = Math.min(min, max);
}
}
System.out.println(min);
}
}