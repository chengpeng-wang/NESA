import java.util.*;
public class Main {
public static void main(String[] args) {
Scanner scan = new Scanner(System.in);
long n = scan.nextLong();
ArrayList<Character> list = new ArrayList<Character>();
while(n > 0) {
int amari = (int) (n % 26);
list.add(toS(amari));
if(n % 26 == 0) {
n -= 1;
}
n /= 26;
}
Collections.reverse(list);
Character[] name = list.toArray(new Character[list.size()]);
char[] name2 = new char[list.size()];
for(int i = 0; i < list.size(); i++) {
name2[i] = (char)name[i];
}
String na = new String(name2);
System.out.println(na);
}
public static char toS(int n) {
if(n == 0) {
return 'z';
}
int a = 'a' - 1;
a = n + a;
return (char)a;
}
}