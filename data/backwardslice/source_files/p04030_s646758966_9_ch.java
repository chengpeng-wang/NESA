import java.util.Scanner;
public class Main {
public static void main(String[] args) {
Scanner sc = new Scanner(System.in);
String str = sc.nextLine();
StringBuilder ans = new StringBuilder("");
for(int i = 0; i < str.length(); i++) {
char ch = str.charAt(i);
switch(ch) {
case '0':
case '1':
ans.append(ch);
break;
case 'B':
if(ans.length() != 0) {
ans.deleteCharAt(ans.length() - 1);
}
break;
}
}
System.out.println(ans);
}
}