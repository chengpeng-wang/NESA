import java.util.*;
import java.util.Collections;
import java.util.stream.Collectors;
public class Main {
public static void main(String[] args) {
Scanner sc = new Scanner(System.in);
int N = sc.nextInt();
int K = sc.nextInt();
long total = 0;
ArrayList<Integer> list = new ArrayList<Integer>();
for (int i = 0; i < N; i++) {
list.add(sc.nextInt());
}
Collections.sort(list);
for (int i = 0; i < N - K; i++) {
total += list.get(i);
}
System.out.println(total);
}
}