import java.util.Scanner;
public class Main {
public static void main(String[] args) {
Scanner sc = new Scanner(System.in);
int A = sc.nextInt();
long ans = 0;
Main main = new Main();
for (int i = 1; i <= A; i++) {
for (int j = 1; j <= A; j++) {
for (int k = 1; k <= A; k++) {
ans += main.GCD(i, main.GCD(j, k));
}
}
}
System.out.println(ans);
}
public int GCD(int num1, int num2) {
if (num2 == 0) {
return num1;
} else {
return GCD(num2, num1 % num2);
}
}
}