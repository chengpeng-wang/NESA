import java.util.*;
public class Main{
public static void main(String[] args){
Scanner sc = new Scanner(System.in);
long n = sc.nextLong();
long a=0;
boolean warikire = false;
for(int i=2; i<=Math.sqrt(n); i++){
if( n%i == 0 ){
a = i;
warikire = true;
}
}
if(warikire){
System.out.println( keta(n/a) );
}else{
System.out.println( keta(n) );
}
return;
}
public static int keta( long a ){
int ans = 1;
while( a/10 != 0 ){
a /= 10;
ans++;
}
return ans;
}
}