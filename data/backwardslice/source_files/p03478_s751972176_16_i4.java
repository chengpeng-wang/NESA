import java.util.Scanner;
public class Main {
public static void main(String[] args) {
Scanner scan = new Scanner(System.in);
int n = scan.nextInt();
int a = scan.nextInt();
int b = scan.nextInt();
int i = 0;
int sum = 0;
for(i = 1; i <= n; i++) {
int i1 = i/10000;
int i2 = (i - (i1 * 10000)) / 1000;
int i3 = (i - (i1 * 10000) - (i2 * 1000)) / 100;
int i4 = (i - (i1 * 10000) - (i2 * 1000) - (i3 * 100)) / 10;
int i5 = (i - (i1 * 10000) - (i2 * 1000) - (i3 * 100)) % 10;
int sum2 = i1 + i2 + i3 + i4 + i5;
if(a <= sum2 && sum2 <= b) {
sum = sum + i;
}
}
System.out.println(sum);
}
}