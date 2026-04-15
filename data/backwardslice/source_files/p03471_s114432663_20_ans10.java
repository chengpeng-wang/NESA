import java.util.*; 
class Main{
public static void main(String[] args){
Scanner sc=new Scanner(System.in);
int n=sc.nextInt();
int y=sc.nextInt();
int ans10=-1;
int ans5=-1;
int ans1=-1;
for(int c=n;c>=0;c--){
for(int d=n-c;d>=0;d--){
int e=n-c-d;
if((c*10000+d*5000+e*1000)==y){
ans10=c;
ans5=d;
ans1=e;
break;
}
}
if(ans10!=-1){
break;
}
}
System.out.println(ans10);
System.out.println(ans5);
System.out.println(ans1);
}
}