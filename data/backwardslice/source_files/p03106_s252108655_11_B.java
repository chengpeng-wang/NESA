import java.util.*;
public class Main {
public static void main(String[] args) {
Scanner sc=new Scanner(System.in);
int A=sc.nextInt();
int B=sc.nextInt();
int K=sc.nextInt();
int x[]=new int[B];
int y=0;
for(int i=1;i<=B;i++) {
if(A%i==0&&B%i==0) {
x[y]=i;
y++;
}
}
System.out.println(x[y-K]);
}
}