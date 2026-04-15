import java.util.Scanner;
public class Main {
public static void main(String[] args) {
Scanner sc = new Scanner(System.in);
int N = sc.nextInt();
int Y = sc.nextInt();
int tenThousand = -1;
int fiveThousand = -1;
int oneThousand = -1;
int sum = 0;
for (int i = 0; i <= N; i++) {
for (int j = 0; i + j <= N; j++) {
for (int k = 0; i + j + k <= N; k++) {
if (i + j + k == N) {
sum = 10000 * i + 5000 * j + 1000 * k;
if (sum == Y) {
tenThousand = i;
fiveThousand = j;
oneThousand = k;
}
}
}
}
}
System.out.println(tenThousand + " " + fiveThousand + " " + oneThousand);
}
}