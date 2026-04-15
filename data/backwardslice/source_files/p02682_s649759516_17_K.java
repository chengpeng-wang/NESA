import java.util.Scanner;
public class Main {
public static void main(String[] args) throws Exception {
Scanner sc = new Scanner(System.in);
int A = sc.nextInt();
int B = sc.nextInt();
int C = sc.nextInt();
int K = sc.nextInt();
int res = 0;
res+=Math.min(A, K);
K -= res;
if(K==0) {
System.out.println(res);
System.exit(0);
}
K-=B;
if(K>0) {
res-=Math.min(K, C);
}
System.out.println(res);
}
}