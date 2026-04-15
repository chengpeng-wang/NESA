import java.util.LinkedHashSet;
import java.util.Scanner;
import java.util.Set;
public class Main {
public static void main(String[] args) {
Scanner scan = new Scanner(System.in);
int n = scan.nextInt();
String[] list = new String[n];
for (int i = 0; i < list.length; i++) {
list[i] = scan.next();
}
char previous = list[0].charAt(list[0].length() - 1);
String ans = "Yes";
Set<String> linkedHashSet = new LinkedHashSet<String>();
for (int i = 0; i < list.length; i++) {
linkedHashSet.add(list[i]);
}
if(linkedHashSet.size() != list.length) {
ans = "No";
} else {
for (int i = 1; i < list.length; i++) {
if (previous != list[i].charAt(0)) {
ans = "No";
break;
}
previous = list[i].charAt(list[i].length() - 1);
}
}
System.out.println(ans);
}
}