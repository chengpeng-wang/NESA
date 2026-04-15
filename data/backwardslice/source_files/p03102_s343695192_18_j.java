import java.util.Scanner;
import java.util.ArrayList;
import java.util.Collections;
public class Main {
public static void main(String[] args) {
Scanner sc = new Scanner(System.in);
int N = sc.nextInt();
int M = sc.nextInt();
int C = sc.nextInt();
int cnt = 0;
int total = 0;
int[] A = new int[M];
for (int i = 0; i < M; i++) {
A[i] = sc.nextInt();
}
for (int i = 0; i < N; i++) {
total = 0;
for (int j = 0; j < M; j++) {
total += A[j] * sc.nextInt();
}
if (total + C > 0) {
cnt++;
}
}
System.out.println(cnt);
}
}