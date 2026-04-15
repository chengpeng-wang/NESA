import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
public class Main {
public static void main(final String[] args) throws Exception {
Scanner sc = new Scanner(System.in);
int N = sc.nextInt();
int K = sc.nextInt();
int[] d = new int[K];
Set<String> A = new HashSet<>();
for (int i = 0; i < K; i++) {
d[i] = sc.nextInt();
for (int j = 0; j < d[i]; j++) {
A.add(sc.next());
}
}
sc.close();
System.out.println(N - A.size());
}
}