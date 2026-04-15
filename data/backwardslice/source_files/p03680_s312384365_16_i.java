import java.util.HashMap;
import java.util.Scanner;
public class Main {
public static void main(String[] args) {
Scanner sc = new Scanner(System.in);
int N = sc.nextInt();
HashMap<Integer, Integer> map = new HashMap<>();
for (int i = 1; i <= N; i++) {
map.put(i, sc.nextInt());
}
int cnt = 0;
boolean can = false;
for (int i = 1; cnt < N;) {
cnt++;
i = map.get(i);
if (i ==  2) {
can = true;
break;
}
}
if (can) {
System.out.println(cnt);
} else {
System.out.println(-1);
}
}
}