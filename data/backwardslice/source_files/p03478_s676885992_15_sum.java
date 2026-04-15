import java.util.Scanner;
public class Main {
public static void main(String[] args) {
Scanner scanner = new Scanner(System.in);
int n = scanner.nextInt();
int a = scanner.nextInt();
int b = scanner.nextInt();
int total = 0;
for (int i = 1; i <= n; i++) {
int one = i % 10;
int ten = Math.floorDiv(i % 100, 10);
int hundred = Math.floorDiv(i % 1000, 100);
int thousand = Math.floorDiv(i % 10000, 1000);
int man = (i == 10000)? 1 : 0;
int sum = one + ten + hundred + thousand + man;
if (a <= sum && sum <= b) {
total += i;
}
}
System.out.println(total);
}
}