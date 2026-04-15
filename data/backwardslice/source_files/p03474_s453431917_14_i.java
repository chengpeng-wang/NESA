import java.util.*;
public class Main {
public static void main(String[] args) {
Scanner scan = new Scanner(System.in);
int a = scan.nextInt();
int b = scan.nextInt();
String s = scan.next();
boolean out = false;
if(s.charAt(a) != '-'){
System.out.println("No");
System.exit(0);
}
for(int i = 0; i <= a + b; i++){
if(i != a && s.charAt(i) == '-'){
System.out.println("No");
System.exit(0);
}
}
System.out.println("Yes");
}
}