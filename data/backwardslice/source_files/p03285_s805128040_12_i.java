import java.util.Scanner;
public class Main {
public static void main(String[] args) {
Scanner sc = new Scanner(System.in);
StringBuilder sb = new StringBuilder();
int n = sc.nextInt();
int[] v = { 4, 7 };
boolean[] dp = new boolean[n + 1];
dp[0] = true;
for (int i = 0; i < 2; i++) {
for (int j = 0; j < n + 1; j++) {
if (j >= v[i]) {
if (dp[j - v[i]]) {
dp[j] = true;
}
}
}
}
if (dp[n]) {
System.out.println("Yes");
} else {
System.out.println("No");
}
}
}