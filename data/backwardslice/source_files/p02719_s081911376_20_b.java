import java.util.*;
import java.lang.*;
public class Main{
public static void main(String[] args){
Scanner sc = new Scanner(System.in);
long N = sc.nextLong();
long K = sc.nextLong();
long A = N%K;
long ans = 0;
if( N<K ){
long a = K-N;
ans = Math.min( a,N );
}
if( N%K==0 ){
ans = 0;
}
if( !(N<K) && !(N%K==0) ){
long a = A;
long b = K-A;
if( a<b ){
ans = a;
}else{
ans = b;
}
}
System.out.println(ans);
}
}