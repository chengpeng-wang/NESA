import java.util.Scanner;
public class Main {
public static void main(String[] args) {
Scanner sc = new Scanner(System.in);
int N,A,B,ans=0;
N = sc.nextInt();
A = sc.nextInt();
B = sc.nextInt();
for(int i = 1; i <= N; ++i) {
int C = i, D=0;
while(C > 0) {
D += C%10;
C /= 10;
}
if(A <= D && D <= B) {
ans+=i;
}
}
System.out.println(ans);
}
}