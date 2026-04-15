import java.util.Scanner;
public class Main {
public static void main(String[] args) {
Scanner sc = new Scanner(System.in);
int n = Integer.parseInt(sc.next());
int k = Integer.parseInt(sc.next());
double p = Math.pow(n, -1);
double ans = 0;
for (int i = 0; i < n; i++) {
if (i + 1 > k) {
ans += 1;
} else {
double pow = Math.log(k / (i + 1.0)) / Math.log(2);
ans += Math.pow(0.5, Math.ceil(pow));
}
}
System.out.println(ans * p);
}
}