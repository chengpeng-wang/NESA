import java.util.Scanner;
public class Main {
public static void main(String[] args) {
Scanner sc = new Scanner(System.in);
String s = sc.next();
char []a = s.toCharArray();
int mina = s.length();
int maxz = 0;
for(int i = 0; i < s.length(); i++) {
if(a[i] == 'A') {
mina = Math.min(mina, i);
}
if(a[i] == 'Z') {
maxz = Math.max(maxz, i);
}
}
System.out.println((maxz - mina) + 1);
}
}