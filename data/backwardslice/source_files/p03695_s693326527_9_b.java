import java.util.*;
public class Main {
public static void main(String[] args) throws Exception {
Scanner sc = new Scanner(System.in);
int n=sc.nextInt();
int[] a=new int[9];
for(int c=0;c<n;c++){
int b=sc.nextInt();
if(b<3200){
a[b/400]++;
}else{
a[8]++;
}
}
int ans=0;
for(int d=0;d<8;d++){
if(a[d]>0){
ans++;
}
}
System.out.println(Math.max(ans,1));
System.out.println(ans+a[8]);
}
}