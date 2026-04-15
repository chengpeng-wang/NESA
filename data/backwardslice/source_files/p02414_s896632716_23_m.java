import java.util.Scanner;
public class Main {
public static void main(String[] args) {
Scanner stdin = new Scanner(System.in);
int n = stdin.nextInt();
int m = stdin.nextInt();
int l = stdin.nextInt();
int[][] A = new int[n][m];
int[][] B = new int[m][l];
for (int i = 0; i < n; i++) {
for (int j = 0; j < m; j++) {
A[i][j] = stdin.nextInt();
}
}
for (int i = 0; i < m; i++) {
for (int j = 0; j < l; j++) {
B[i][j] = stdin.nextInt();
}
}
for (int i = 0; i < n; i++) {
for (int j = 0; j < l; j++) {
long sum = 0;
for (int k = 0; k < m; k++) {
sum += A[i][k] * B[k][j];
}
System.out.printf(j == l - 1 ? "%d%n" : "%d ", sum);
}
}
}
}