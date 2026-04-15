import java.util.Scanner;
public class Main{
public static void main(String[]args){
int pos=-1;
Scanner sc=new Scanner(System.in);
int[] a=new int[5];
for(int i=0;i<5;i++){
a[i]=sc.nextInt();
if(a[i]==0)
pos=i;
}
System.out.println(pos+1);
}
}