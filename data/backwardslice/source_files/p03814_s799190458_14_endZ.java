import java.util.*;
public class Main {
public static void main(String[] args) {
Scanner sc = new Scanner(System.in);
String s = sc.next();
int startA = 0;
boolean isA = false;
int endZ = 0;
for (int i=0; i<s.length();i++) {
if (!isA && "A".equals(s.substring(i,i+1))) {
startA = i;
isA = true;
} else if ("Z".equals(s.substring(i,i+1))) {
endZ = i;
}
}
System.out.println(endZ - startA +1);
}
}