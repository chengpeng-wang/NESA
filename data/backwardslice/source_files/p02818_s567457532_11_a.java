import java.util.Scanner;
public class Main{
public static void main(String[] args){
Scanner scan = new Scanner(System.in);
long a = scan.nextLong();
long b = scan.nextLong();
long k = scan.nextLong();
if(a >= k){
long x = a-k;
System.out.println(x + " " + b);
}else if(a < k && a+b >= k){
long x = a+b-k;
System.out.println(0 + " " + x);
}else{
System.out.println(0 + " " + 0);
}
}
}