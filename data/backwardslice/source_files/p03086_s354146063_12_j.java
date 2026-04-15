import java.io.*;
import java.util.*;
class Main {
public static void main(String[] args) throws Exception {
final Scanner sc = new Scanner(System.in);
String s = sc.next();
int a = 0;
Set<Character> valid = new HashSet<>(Arrays.asList('A', 'G', 'C', 'T'));
for (int i = 0; i < s.length(); i++) {
int count = 0;
int j = i;
while (j < s.length() && valid.contains(s.charAt(j++))) count++;
a = Math.max(a, count);
}
System.out.println(a);
}
}