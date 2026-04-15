import java.util.*;
public class Main {
public static void main(String[] args ) throws Exception {
Scanner sc = new Scanner(System.in);
int N = sc.nextInt();
int answer = 0;
List<Integer> ds = new ArrayList<>();
for (int z = 0; z < N; z++) {
ds.add(sc.nextInt());
}
for(int i = 0; i < N - 1; i++) {
for(int j = i+1; j < N; j++) {
answer += ds.get(i) * ds.get(j);
}
}
System.out.println(answer);
}
}