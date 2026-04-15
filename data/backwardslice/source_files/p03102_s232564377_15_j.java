import java.util.Scanner;
public class Main {
public static void main(String[] args) {
Scanner sc = new Scanner(System.in);
int N = sc.nextInt();
int M = sc.nextInt();
int C = sc.nextInt();
int[] B = new int[M];
for (int i = 0; i < M; i++) {
B[i] = sc.nextInt();
}
int counter = 0;
for (int i = 0; i < N; i++) {
int judge = 0;
for (int j = 0; j < M; j++) {
judge += sc.nextInt() * B[j];
}
if (judge + C > 0)
counter++;
}
System.out.println(counter);
}
}