import java.util.Scanner;
public class Main {
public static void main(String[] args){
Scanner scan = new Scanner(System.in);
int t = scan.nextInt();
int u = scan.nextInt();
int n = t + 1;
int m = u + 1;
int[][] a = new int[n][m];
int i,r;
for(i = 0; i < n - 1; ++i){
for(r = 0; r < m - 1; ++r){
a[i][r] = scan.nextInt();
a[i][m - 1] += a[i][r];
a[n - 1][r] += a[i][r];
a[n - 1][m - 1] += a[i][r];
}
}
for(i = 0; i < n; ++i){
for(r = 0; r < m; ++r){
if(r == m - 1){
System.out.println(a[i][r]);
}
else
System.out.print(a[i][r]+" ");
}
}
}
}