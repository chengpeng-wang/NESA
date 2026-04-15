import java.util.Scanner;
public class Main {
public static void main(String[] args) {
Scanner sc = new Scanner(System.in);
int a = sc.nextInt();
int b = sc.nextInt();
int c = sc.nextInt();
int x = sc.nextInt();
int sum = 0;
int count = 0;
for (int i = 0; i <= a; i++) {
for (int j = 0; j <= b; j++) {
for (int j2 = 0; j2 <= c; j2++) {
sum = (500 * i) + (100 * j) + (50 * j2);
if(x == sum) {
count ++;
}
}
}
}
System.out.println(count);
}
}