import java.util.*;
public class Main{
static public void  main(String[] args){
Scanner scan = new Scanner(System.in);
int N = scan.nextInt();
int M = scan.nextInt();
int C = scan.nextInt();
int[] b = new int[M];
int[][] a = new int[N][M];
for(int i = 0; i < M;i++){
b[i] = scan.nextInt();
}
int ret = 0;
for(int i = 0; i < N;i++){
int sum = C;
for(int j = 0; j < M;j++){
sum += b[j] * scan.nextInt();
}
if(sum > 0) ret++;
}
System.out.println(ret);
}
}