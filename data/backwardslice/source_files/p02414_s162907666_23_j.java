import java.util.Scanner;
public class Main {
public static void main(String[] args) {
Scanner scan = new Scanner(System.in);
int m = scan.nextInt();
int n = scan.nextInt();
int l = scan.nextInt();
int[][] data = new int[m][n];
int[][] datac = new int[n][l];
long sum = 0;
for(int i = 0 ; i < m ; i++){
for(int j = 0 ; j < n ; j++){
data[i][j]=scan.nextInt();
}
}
for(int i = 0 ; i < n; i ++){
for(int j = 0 ; j < l ; j++){
datac[i][j]=scan.nextInt();
}
}
for(int i = 0 ; i < m ; i++){
for(int k = 0 ; k < l ; k++){
for(int j = 0 ; j  <n; j++){
sum+=data[i][j]*datac[j][k];
}
System.out.print(sum);
if(k+1<l) System.out.print(" ");
sum=0;
}
System.out.println("");
}
}
}