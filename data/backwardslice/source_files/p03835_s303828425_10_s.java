import java.util.Scanner;
public class Main {
public static void main(String[] args) {
Scanner sc = new Scanner(System.in);
int k = Integer.parseInt(sc.next());
int s = Integer.parseInt(sc.next());
int ans = 0;
for (int i = 0; i <= k; i++) {
for(int j = 0; j <= k; j++) {
int l = s - i - j;
if (l >= 0 && l <= k) {
ans++;
}
}
}
System.out.println(ans);
}
}