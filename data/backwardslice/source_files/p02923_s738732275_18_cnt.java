import java.util.Scanner;
public class Main {
public static void main(String[] args) throws Exception {
Scanner sc = new Scanner(System.in);
int n = sc.nextInt();
int[] h = new int[n];
for (int i = 0; i < n; i++) {
h[i] = sc.nextInt();
}
sc.close();
int ans = 0;
int cnt = 0;
for (int i = 1; i < n; i++) {
if (h[i - 1] >= h[i]) {
cnt++;
ans = Math.max(ans, cnt);
} else {
cnt = 0;
}
}
System.out.println(ans);
}
}