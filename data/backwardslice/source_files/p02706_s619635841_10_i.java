import java.util.Scanner;
public class Main {
public static void main(String[] args) {
Scanner sc = new Scanner(System.in);
long N = sc.nextLong();
int M = sc.nextInt();
long[] A = new long[M];
long ans = N;
for(int i=0;i<M;i++){
A[i] = sc.nextInt();
ans -= A[i];
}
System.out.println(ans >= 0 ? ans : -1);
}
}