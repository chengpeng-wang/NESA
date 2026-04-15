import java.util.Scanner;
public class Main {
public static void main(String[] args){
Scanner scan = new Scanner(System.in);
int n = scan.nextInt();
int[] a = new int[n];
for(int b = 0;b<n;b++){
a[b] = scan.nextInt();
}
for(int d = 1;d<=n;d++){
System.out.print(a[n-d]);
if(d==n){
System.out.println("");
}else{
System.out.print(" ");
}
}
}
}