import java.util.*;
import java.util.TreeMap;
public class Main {
public static void main(String[] args) {
Scanner sc = new Scanner(System.in);
int N = sc.nextInt();
int M = sc.nextInt();
int C = sc.nextInt();
ArrayList<Integer> list = new ArrayList<Integer>();
int ans = 0;
for (int i = 0; i < M; i++) {
list.add(sc.nextInt());
}
for (int i = 0; i < N; i++) {
int total = 0;
for (int j = 0; j < M; j++) {
total += list.get(j) * sc.nextInt();
}
if (total + C > 0) {
ans++;
}
}
System.out.println(ans);
}
}