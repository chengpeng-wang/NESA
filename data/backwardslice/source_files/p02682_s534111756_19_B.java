import java.util.Scanner;
public class Main {
public static void main(String[] args) {
Scanner sc = new Scanner(System.in);
long A = sc.nextInt();
long B = sc.nextInt();
long C = sc.nextInt();
long K = sc.nextInt();
long ans = 0;
if (A < K){
ans = A;
K -= A;
} else {
ans = K;
System.out.println(ans);
return;
}
if (B < K){
K -= B;
} else {
System.out.println(ans);
return;
}
ans += -1 * K;
System.out.println(ans);
}
}