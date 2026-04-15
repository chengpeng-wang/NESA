import java.util.*;
class Main{
public static void main(String[] args){
Scanner sc=new Scanner(System.in);
int N=sc.nextInt();
int A=sc.nextInt();
int B=sc.nextInt();
int sum=0;
int temp=0;
int tempSum=0;
for(int i=1;i<=N;i++){
temp=i;
tempSum=0;
while(temp/10 != 0){
tempSum+=temp%10;
temp/=10;
}
tempSum+=temp;
if(tempSum>=A && tempSum<=B){
sum+=i;
}
}
System.out.println(sum);
}
}