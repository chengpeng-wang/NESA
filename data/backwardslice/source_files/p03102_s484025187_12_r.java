import java.util.Scanner;
public class Main {
public static void main(String[] args) {
Scanner scanner = new Scanner(System.in);
int N = scanner.nextInt();
int M = scanner.nextInt();
int C = scanner.nextInt();
int[] B = new int[M];
for (int i = 0; i < M; i++) B[i] = scanner.nextInt();
int tot = 0;
for (int i = 0; i < N; i++) {
int r = C;
for (int j = 0; j < M; j++) r += scanner.nextInt() * B[j];
if (r > 0) tot++;
}
System.out.println(tot);
}
}