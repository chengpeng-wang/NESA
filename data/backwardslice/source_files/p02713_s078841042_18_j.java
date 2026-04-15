import java.util.*;
import java.math.BigInteger;
public class Main{
public static int gcd(int a, int b, int c){
return calculateGcd(calculateGcd(a, b), c);
}
public static int calculateGcd(int a, int b) {
if (a == 0) return b;
if (b == 0) return a;
if (a > b) return calculateGcd(b, a % b);
return calculateGcd(a, b % a);
}
public static void main(String[] args){
Scanner sc = new Scanner(System.in);
int a = sc.nextInt();
BigInteger sum = BigInteger.valueOf(0);
for(int i=1;i<=a;i++){
for(int j=1;j<=a;j++){
for(int k=1;k<=a;k++){
sum=sum.add(BigInteger.valueOf(gcd(i,j,k)));
}
}
}
System.out.println(sum);
}
}