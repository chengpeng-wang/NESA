import java.util.*;
class Main{
public static void main(String[] $){
Scanner s=new Scanner(System.in);
int n=s.nextInt();
long r=0;
int[] b=new int[n];
Arrays.fill(b,100000);
for(int i=1;i<n;++i){
int a=s.nextInt();
b[i-1]=Math.min(b[i-1],a);
b[i]=Math.min(b[i],a);
}
System.out.println(Arrays.stream(b).sum());
}
}