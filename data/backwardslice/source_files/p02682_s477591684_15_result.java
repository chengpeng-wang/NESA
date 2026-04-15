import java.util.Scanner;
public class Main {
public static void main(String[] args) {
Scanner scan = new Scanner(System.in);
int a = scan.nextInt();
int b = scan.nextInt();
int c = scan.nextInt();
int k = scan.nextInt();
int result = 0;
if(a >= k) {
result += k;
} else if(a + b >= k) {
result += a;
} else {
result += ( a - (k - a - b));
}
System.out.println(String.valueOf(result));
}
}