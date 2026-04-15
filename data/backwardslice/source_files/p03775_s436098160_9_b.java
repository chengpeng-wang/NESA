import java.util.*;
public class Main{
public static void main(String[] args){
Scanner sc=new Scanner(System.in);
long n=Long.parseLong(sc.next());
sc.close();
for(long a=(long) Math.sqrt(n);a>=1;a--){
if(n%a==0){
long b=n/a;
String b_str=Long.toString(b);
System.out.println(b_str.length());
System.exit(0);
}
}
}
}