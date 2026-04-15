import java.util.Scanner;
import java.lang.Math;
class Main{
public static void main(String[] args){
Scanner scanner = new Scanner(System.in);
long n = scanner.nextLong();
long a = scanner.nextLong();
long b = scanner.nextLong();
scanner.close();
long result1 = n/(a+b)*a;
long result2 = n%(a+b);
if(result2>=a){
System.out.println(result1+a);
return;
}else{
System.out.println(result1+result2);
return;
}
}
}