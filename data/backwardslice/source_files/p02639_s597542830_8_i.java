import java.util.Scanner;
public class Main {
public static void main(String[] args) {
Scanner scn=new Scanner(System.in);
int []arr=new int [5];
int ans=0;
for(int i=0;i<5;i++) {
arr[i]=scn.nextInt();
if(arr[i]==0)
ans=i+1;
}
System.out.println(ans);
}
}