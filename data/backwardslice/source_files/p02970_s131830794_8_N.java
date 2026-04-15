import java.util.Scanner;
public class Main {
public static void main(String[] args) {
Scanner sc = new Scanner(System.in);
int N = sc.nextInt();
int D = sc.nextInt();
int ans;
if(N%(D+1+D)>0) {
ans = N / (D + 1 + D)+1;
} else {
ans = N / (D + 1 + D);
}
System.out.println(ans);
}
}