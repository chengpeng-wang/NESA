import java.util.*;
public class Main {
public static void main(String[] args) {
Scanner sc = new Scanner(System.in);
int a = sc.nextInt();
int b = sc.nextInt();
int k = sc.nextInt();
List<Integer> list = new ArrayList<>();
int min = Math.min(a,b);
for (int i = 1; i<=min;i++) {
if (a % i == 0 && b % i == 0) list.add(i);
}
list.sort(Collections.reverseOrder());
System.out.println(list.get(k - 1));
}
}