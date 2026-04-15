import java.util.Scanner;
public class Main {
public static void main(String[] args) {
Scanner sc = new Scanner(System.in);
int N = sc.nextInt();
int K = sc.nextInt();
String S = sc.next();
sc.close();
for(int i = 0; i < N; i++) {
if(i == K-1) {
String ans = String.valueOf(S.charAt(K - 1)).toLowerCase();
System.out.print(ans);
}else {
System.out.print(S.charAt(i));
}
}
}
}