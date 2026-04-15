import java.util.Scanner;
public class Main {
public static void main(String[] args) throws Exception {
Scanner sc = new Scanner(System.in);
int K = sc.nextInt();
int S = sc.nextInt();
int ans = 0;
for (int x = 0; x <= K; x++) {
for (int y = 0; y <= K; y++) {
if (x+y > S)
break;
if (S - (x + y) <= K) {
ans++;
}
}
}
System.out.println(ans);
}
}	