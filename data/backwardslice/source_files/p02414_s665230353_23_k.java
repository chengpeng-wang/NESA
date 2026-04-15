import java.util.*;
public class Main {
public static void main(String[] args){
Scanner sc= new Scanner(System.in);
int n=sc.nextInt();
int m=sc.nextInt();
int l=sc.nextInt();
int a[][] = new int[n][m];
int b[][] = new int[m][l];
for(int i=0;i<n;i++){
for(int j=0;j<m;j++){
a[i][j]=sc.nextInt();
}
}
for(int i=0;i<m;i++){
for(int j=0;j<l;j++){
b[i][j]=sc.nextInt();
}
}
for(int i=0;i<n;i++){
for(int j=0;j<l;j++){
double sum=0;
for(int k=0;k<m;k++){
sum+=a[i][k]*b[k][j];
}
System.out.printf("%.0f",sum);
if(j!=l-1){
System.out.print(" ");
}
}
System.out.println();
}
}
}