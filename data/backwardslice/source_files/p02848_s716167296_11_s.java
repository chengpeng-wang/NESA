import java.util.*;
public class Main {
public static void main(String[] args) {
Scanner sc = new Scanner(System.in);
int n = sc.nextInt();
String sList = sc.next();
sc.close();
String r = "";
for (char s : sList.toCharArray()) {
s += n;
if ('Z' < s) {
s -= 26;
}
r += (char) s;
}
System.out.println(r);
}
}