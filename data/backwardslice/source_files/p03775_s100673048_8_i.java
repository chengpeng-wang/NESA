import java.util.Scanner;
public class Main {
public static void main(String[] args){
Scanner io = new Scanner(System.in);
long N = io.nextLong();
long ans = Long.MAX_VALUE;
for(long i=1;i*i<=N;i++){
if(N%i==0){
ans = Math.min(ans,
Math.max(String.valueOf(i).length(),
String.valueOf(N/i).length())
);
}
}
System.out.println(ans);
}
}