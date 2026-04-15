import java.util.Scanner;
public class Main {
public static void main(String[] args) {
Scanner i=new Scanner(System.in);
int n=i.nextInt();
int[] a=new int[n+1];
for(int s=0;s<n;s++){
a[s]=i.nextInt();
}
int min=Integer.MAX_VALUE;
int max=Integer.MIN_VALUE;
long gt=0;
for(int s=0;s<n;s++){
if(min>a[s]){min=a[s];}
if(max<a[s]){max=a[s];}
gt+=a[s];
}
System.out.println(min+" "+max+" "+gt);
}
}