import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
class Main {
public static void main(String[] args) {
Scanner scanner = new Scanner(System.in);
int n = scanner.nextInt();
int k = scanner.nextInt();
List<Integer> hps = new ArrayList<>(n);
for (int i = 0; i < n; i++) {
int hp = scanner.nextInt();
hps.add(hp);
}
hps.sort(Comparator.reverseOrder());
long result = 0;
for (int i = k; i < n; i++) {
result += hps.get(i);
}
System.out.println(result);
}
}