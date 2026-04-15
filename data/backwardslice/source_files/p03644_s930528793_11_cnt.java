import java.util.Scanner;
public class Main {
public static void main(String[] args) {
Scanner sc = new Scanner(System.in);
int n = sc.nextInt();
int ans = 1;
for (int i = n; 0 < i; i--) {
int cnt = 0;
for (int j = 2;; j *= 2) {
if (i % j == 0) {
cnt = j;
} else {
break;
}
}
ans = Math.max(ans, cnt);
}
System.out.println(ans);
}
}