import java.util.Scanner;
public class Main {
public static void main(String[] args) {
Scanner sc = new Scanner(System.in);
int N = sc.nextInt();
int M = sc.nextInt();
int C = sc.nextInt();
int[] B = new int[M];
for(int j = 0; j < M; j++) {
B[j] = sc.nextInt();
}
int count = 0;
for(int i = 0; i < N; i++) {
int sum = 0;
for(int j = 0; j < M; j++) {
sum += B[j] * sc.nextInt();
}
if (sum > -1 * C) {
count++;
}
}
System.out.println(count);
}
}