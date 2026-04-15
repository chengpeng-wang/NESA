import java.util.ArrayList;
import java.util.Scanner;
public class Main {
public static void main(String[] args) {
Scanner sc = new Scanner(System.in);
int N = sc.nextInt();
ArrayList<Integer> list = new ArrayList<>();
for (int i = 0; i < N; i++) {
list.add(sc.nextInt());
}
sc.close();
int ans = 0;
int active = 0;
for (int i = 0; i < N; i++) {
int tmp = list.get(i);
if (active < tmp) {
ans += tmp - active;
}
active = tmp;
}
System.out.println(ans);
}
}