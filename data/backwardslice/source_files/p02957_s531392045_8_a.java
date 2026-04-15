import java.util.*;
public class Main {
public static void main(String[] args){
Scanner sc = new Scanner(System.in);
long a = sc.nextLong();
long b = sc.nextLong();
sc.close();
if((a + b) % 2 == 0){
System.out.println((a + b) / 2);
}else{
System.out.println("IMPOSSIBLE");
}
}
}