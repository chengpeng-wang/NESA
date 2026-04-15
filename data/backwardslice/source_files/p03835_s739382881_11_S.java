import java.util.Scanner;
public class Main {
public static void main(String[] args) {
Scanner sc = new Scanner(System.in);
int K = sc.nextInt();
int S = sc.nextInt();
sc.close();
int cnt = 0;
for(int x = 0; x <= K; x++) {
for(int y = 0; y <= K; y++) {
int t = S - x - y;
if(t >= 0 && t <= K) {
cnt++;
}
}
}
System.out.println(cnt);
}
}