import java.util.*;
public class Main {
public static void main(String[] args) throws Exception {
Scanner sc = new Scanner(System.in);
int n = sc.nextInt();
int cnt = 0;
for (int i = 1; i <= n; i++) {
if (i % 2 == 1) {
cnt++;
}
}
double ans = (double)cnt / (double)n;
System.out.println(ans);
}
}