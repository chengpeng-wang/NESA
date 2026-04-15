import java.util.Scanner;
public class Main {
public static void main(String[] args) {
Scanner stdIn = new Scanner(System.in);
int a = stdIn.nextInt();
int b = stdIn.nextInt();
int c = stdIn.nextInt();
int count = 0;
for (int i = a; i <= b; i++) {
if (c % i == 0) count++;
}
System.out.println(count);
}
}