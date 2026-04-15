import java.util.Scanner;
public class Main {
public static void main(String[] args) {
Scanner sc = new Scanner(System.in);
int n = sc.nextInt();
int max = Integer.MAX_VALUE;
int a, cnt = 0;
for (int i = 0; i < n; i++) {
a = sc.nextInt();
cnt = 0;
while (a % 2 == 0) {
a = a / 2;
cnt++;
}
max = Math.min(max, cnt);
}
System.out.println(max);
}
}