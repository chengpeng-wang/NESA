import java.util.Scanner;
public class Main {
public static void main(String[] args) {
Scanner sc = new Scanner(System.in);
int N = sc.nextInt();
String S = sc.next();
sc.close();
int a = 0;
for(int i=0; i<N; i++) {
if(S.charAt(i)=='A' && i<N-2) {
if(S.charAt(i+1)=='B' && S.charAt(i+2)=='C') {
a++;
}
}
}
System.out.println(a);
}
}