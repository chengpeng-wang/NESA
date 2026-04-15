import java.util.*;
public class Main {
public static void main(String[] args) throws Exception{
Scanner sc = new Scanner(System.in);
long w = sc.nextLong();
long h = sc.nextLong();
long x = sc.nextLong();
long y = sc.nextLong();
double ans = w * h / 2.0;
System.out.print(ans + " ");
if(x * 2 == w && y * 2 == h){
System.out.println(1);
}else{
System.out.println(0);
}
}
}