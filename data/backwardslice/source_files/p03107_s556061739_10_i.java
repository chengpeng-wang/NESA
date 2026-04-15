import java.util.Scanner;
public class Main{
public static void main(String[] args) {
Scanner sc=new Scanner(System.in);
String S=sc.next();
int A=0;
int B=0;
char[] c=S.toCharArray();
for(int i=0;i<S.length();i++) {
if(c[i]=='1') {
A++;
}else {
B++;
}
}
System.out.println(2*Math.min(A,B));
}
}