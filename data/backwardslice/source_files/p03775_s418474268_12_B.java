import java.util.*;
public class Main{
public static void main(String[] args){
Scanner sc = new Scanner(System.in);
long N = sc.nextLong();
int M = (int)  Math.sqrt( N ) + 1;
int Fmin = String.valueOf(N).length();
for(int i = 2 ; i <= M ; i ++){
if( N % i == 0 ){
long j = N / i ;
int A = String.valueOf(i).length();
int B = String.valueOf(j).length();
int F = Math.max(A,B);
if(Fmin > F){
Fmin = F ;
}
}
}
System.out.println(Fmin);
}
}