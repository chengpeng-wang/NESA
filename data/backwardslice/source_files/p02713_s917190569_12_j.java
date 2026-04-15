import java.util.*;
public class Main{
public static int gcd(int a, int b){
if ( a == 0 ) return b;
return gcd(b%a, a);
}
public static void main(String []args){
Scanner scan = new Scanner(System.in);
int a = scan.nextInt();
long s = 0;
for (int i=1;i<=a;i++){
for (int j=1;j<=a;j++){
for (int k=1;k<=a;k++){
s+=gcd(gcd(i,j), k);
}
}
}
System.out.println(s);
}
}