import java.util.*;
public class Main {
public static void main(String[] args) throws Exception {
Scanner sc = new Scanner(System.in);
String a = sc.next();
int n = a.length();
StringBuilder sb = new StringBuilder();
for (int i = 0; i < n; i++) {
sb.append("x");
}
System.out.println(sb.toString());
}
}