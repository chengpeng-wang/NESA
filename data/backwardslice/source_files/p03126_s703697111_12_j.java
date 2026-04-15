import java.util.Arrays;
import java.util.Scanner;
public class Main{
static long mod = 1000000007;
public static void main(String[] args){
Scanner sc = new Scanner(System.in);
int N = sc.nextInt(), M = sc.nextInt();
int[] eat = new int[M];
Arrays.fill(eat, 0);
for(int i = 0; i < N; i++) {
int K = sc.nextInt();
for(int j = 0; j < K; j++) {
int A = sc.nextInt() - 1;
eat[A]++;
}
}
int ans = 0;
for(int i = 0; i < M; i++) if(eat[i] == N) ans++;
System.out.println(ans);
}
}