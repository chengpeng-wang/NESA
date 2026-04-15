import java.util.*;
public class Main {
public static void main(String[] args) {
Scanner sc = new Scanner(System.in);
int K = Integer.parseInt(sc.next());
int S = Integer.parseInt(sc.next());
int result = 0;
for (int x = 0; x <= K; x++) {
for (int y = 0; y <= K; y++) {
int total = x + y;
if (!(total > S) && (total+K) >= S) {
result++;
}
}
}
System.out.println(result);
}
}