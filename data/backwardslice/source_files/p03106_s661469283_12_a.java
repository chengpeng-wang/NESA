import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
public class Main {
public static void main(String[] args) {
Scanner sc = new Scanner(System.in);
int a = sc.nextInt();
int b = sc.nextInt();
int k = sc.nextInt();
List<Integer> list = new ArrayList<>();
for (int i = 1; i <= Math.max(a, b); i++) {
if (a % i == 0 && b % i == 0) {
list.add(i);
}
}
System.out.println(list.get(list.size() - k));
}
}