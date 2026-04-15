import java.util.*;
class Main{
public static void main(String[] args){
Scanner sc = new Scanner(System.in);
int a = sc.nextInt();
int b = sc.nextInt();
int k = sc.nextInt();
int m = 0;
int c = 0;
for(int i=100;i>=1;i--){
if(0==a%i&&0==b%i){
m++;
if(m==k){
System.out.println(i);
}
}
}
}
}