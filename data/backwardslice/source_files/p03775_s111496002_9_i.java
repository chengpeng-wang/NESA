import java.util.Scanner;
public class Main {
public static void main(String[] args) {
Scanner sc = new Scanner(System.in);
long N = sc.nextLong();
sc.close();
int p = (int)Math.pow(N, 0.5);
int ans = Integer.MAX_VALUE;
for(int i = 1 ; i <= p ; i++){
if(N%i == 0){
ans = Math.min(ans, comp(i,N/i));
}
}
System.out.println(ans);
}
private static int comp(long m, long n) {
if(Long.toString(m).length() >= Long.toString(n).length()){
return Long.toString(m).length();
}
else{
return Long.toString(n).length();
}
}
}