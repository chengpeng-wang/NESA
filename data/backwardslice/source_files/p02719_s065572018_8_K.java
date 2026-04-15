import java.util.Scanner;
public class Main {
public static void main(String[] args) {
Scanner sc = new Scanner(System.in);
long N = sc.nextLong();
long K = sc.nextLong();
long ans;
if (K <= N) {
ans = (N%K) < (K - N%K) ? N%K : (K - N%K);
} else {
ans = N < (K - N) ? N : (K - N);
}
System.out.println(ans);
}
}