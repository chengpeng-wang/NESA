import java.util.*;
public class Main {
public static void main(String[] args) {
Scanner sc = new Scanner(System.in);
int N = sc.nextInt();
int[] A = new int[N];
for(int i = 0; i < N; i++) {
A[i] = sc.nextInt();
}
int tmp, k;
for(int i = 1; i < N; i++) {
for(int j = 0; j < N - 1; j++) {
System.out.print(A[j] + " ");
}
System.out.println(A[N - 1]);
tmp = A[i];
k = i - 1;
while(k >= 0 && A[k] > tmp) {
A[k + 1] = A[k];
k--;
}
A[k + 1] = tmp;
}
for(int i = 0; i < N - 1; i++) {
System.out.print(A[i] + " ");
}
System.out.println(A[N - 1]);
}
}