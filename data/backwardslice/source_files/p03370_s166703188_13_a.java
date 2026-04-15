import java.util.Scanner;
public class Main {
public static void main(String[] args) {
Scanner scan = new Scanner(System.in);
int n = scan.nextInt();
int x = scan.nextInt();
int[] a = new int[n];
int sum = 0;
for (int i = 0; i < n; i++) {
a[i] = scan.nextInt();
sum += a[i];
if (a[i] < a[0]) {
a[0] = a[i];
}
}
int b = x - sum;
System.out.println(b / a[0] + n);
}
}