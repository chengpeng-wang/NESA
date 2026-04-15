import java.util.Scanner;
public class Main {
public static void main(String[] args) {
Scanner sc = new Scanner(System.in);
int N = sc.nextInt();
int[] A = new int[21];
int[] B = new int[21];
int[] C = new int[21];
int i = 0;
int cs = 0;
int csSum = 0;
for( i = 0; i<N; i++) {
A[i] = sc.nextInt();
}
for( i = 0; i<N; i++) {
B[i] = sc.nextInt();
}
for( i = 0; i<N-1; i++) {
C[i] = sc.nextInt();
}
for(i = 1; i<=N; i++) {
int j = A[i-1];
if(A[i] == j+1) {
cs = B[j-1] + C[j-1];
}else {
cs = B[j-1];
}
csSum += cs;
}
System.out.println(csSum);
sc.close();
}
}