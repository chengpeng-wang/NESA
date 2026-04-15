import java.util.*;
public class Main {
public static void main (String[] args) {
Scanner sc = new Scanner(System.in);
String S = sc.next();
sc.close();
String[] s = S.split("");
int red = 0;
int blue = 0;
for (int i = 0; i < s.length; i++) {
if(s[i].equals("0")){
red++;
} else {
blue++;
}
}
System.out.println(s.length - Math.abs(red - blue));
}
}