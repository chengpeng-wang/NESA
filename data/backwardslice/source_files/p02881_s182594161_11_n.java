import java.util.Scanner;
public class Main {
public static void main(String[] args) {
Scanner sc = new Scanner(System.in);
long n = Long.parseLong(sc.next());
long min = n-1;
sc.close();
for(long i=2;i*i<=n;i++){
if(n%i==0){
long x = i;
long y = n/i;
min = Math.min(min,x-1+y-1);
}
}
System.out.println(min);
}
}