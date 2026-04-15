import java.util.*;
public class Main {
public static void main(String[] args) {
Scanner sc = new Scanner(System.in);
int N = sc.nextInt();
int[] A = new int[N];
int[] B = new int[N];
int[] C = new int[N - 1];
for (int i = 0; i < N; i++) {
A[i] = sc.nextInt();
}
for (int i = 0; i < N; i++) {
B[i] = sc.nextInt();
}
for (int i = 0; i < N - 1; i++) {
C[i] = sc.nextInt();
}
int s = 0;
int bx = -2;
for (int i = 0; i < N; i++) {
int x = A[i] - 1;
s += B[x];
if (bx == x - 1) {
s += C[bx];
}
bx = x;
}
System.out.println(s);
}
}