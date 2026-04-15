import java.util.Scanner;
public class Main {
public static void main(String[] args) {
Scanner scanner=new Scanner(System.in);
String s=scanner.next();
String t=scanner.next();
int diffCount=0;
for (int i = 0; i < s.length() && i<t.length(); i++) {
if(s.charAt(i) == t.charAt(i))
diffCount++;
}
System.out.println(diffCount);
}
}