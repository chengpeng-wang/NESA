import java.util.Scanner;
public class Main {
public static void main(String[] args) {
Scanner sc = new Scanner(System.in);
int K = Integer.parseInt(sc.next());
int S = Integer.parseInt(sc.next());
sc.close();
int cnt = 0;
for(int X = 0;X <= K;X++) {
for(int Y = 0;Y <= K;Y++) {
if(S - X - Y <= K && S - X - Y >= 0) {
cnt++;
}
}
}
System.out.println(cnt);
}
}