import java.util.*;
public class Main {
public static void main(String[] args){
Scanner sc = new Scanner(System.in);
String s = sc.next();
char[] ch = s.toCharArray();
int cnt = 0;
if(ch[0] == '1') {
cnt++;
}
if(ch[1] == '1') {
cnt++;
}
if(ch[2] == '1') {
cnt++;
}
System.out.println(cnt);
}
}