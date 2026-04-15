import java.util.*;
public class Main {
public static void main(String[] args) {
Scanner sc = new Scanner(System.in);
int a = sc.nextInt();
int b = sc.nextInt();
int c = sc.nextInt();
if(a%b==0) {
System.out.println(a/b*c);
}else {
System.out.println((a/b+1)*c);
}
}
}