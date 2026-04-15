import java.util.*;
public class Main{
public static void main(String[] args){
Scanner sc=new Scanner(System.in);
int n=sc.nextInt();
int max=-1000000,min=1000000;
double sum=0;
for(int i=0;i<n;i++){
int a=sc.nextInt();
if(a>max){
max=a;
}
if(a<min){
min=a;
}
sum+=a;
}
System.out.printf("%d %d %.0f\n",min,max,sum);
}
}