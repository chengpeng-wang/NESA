import java.util.Scanner;
public class Main {
public static void main(String[] args) {
Scanner sc = new Scanner(System.in);
int n = sc.nextInt();
long max = -1000000;
long min = 1000000;
long sum = 0;
for( int i = 0; i < n; i++ ){
long a = sc.nextLong();
if( max < a )
max = a;
if( a < min )
min = a;
sum += a;
}
System.out.println( min + " " + max + " " + sum);
sc.close();
}
}