import java.util.Scanner;
public class Main {
public static void main(String[] args){
Scanner sc = new Scanner(System.in);
String s = sc.next();
int len = s.length();
int cnt = 0;
int max = 0;
char[] sChars = s.toCharArray();
for(int i=0;i<len;i++) {
if(sChars[i]=='R') {
cnt=cnt+1;
if(max<cnt) {
max=cnt;
}
}else {
cnt=0;
}
}
System.out.println(max);
}	
}