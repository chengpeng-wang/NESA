import java.util.*;
public class Main {
public static void main(String[] args) {
Scanner scan = new Scanner(System.in);
int n = scan.nextInt();
int x = scan.nextInt();
int t = scan.nextInt();
if(n%x == 0) {
System.out.println((n/x)*t);
}else {
System.out.println(((n/x)+1)*t);
}
}
public static int gcd(int x,int y) {
if(x%y == 0) {
return y;
}else {
return gcd(y,x%y);
}
}
}