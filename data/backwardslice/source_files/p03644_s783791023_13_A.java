import java.util.*;
public class Main {
public static void main(String[] args) {
Scanner sc = new Scanner(System.in);
int N = sc.nextInt();
int max = 0;
int cnt = 0;
int ans = 1;
int A = 0;
for (int i = 1; i <= N; i++) {
cnt = 0;
A = i;
while (A % 2 == 0) {
A = A / 2;
cnt++;
}
if (cnt > max) {
ans = i;
max = cnt;
}
}
System.out.println(ans);
}
}