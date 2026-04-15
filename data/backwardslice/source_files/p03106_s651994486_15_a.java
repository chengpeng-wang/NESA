import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
public class Main {
public static void main(String[] args) {
Scanner sc = new Scanner(System.in);
int a = sc.nextInt();
int b = sc.nextInt();
int k = sc.nextInt();
int max = a > b ? a : b;
List<Integer> list = new ArrayList<Integer>();
for (int i = 1; i <= max; i++) {
if (a % i == 0 && b % i == 0) {
list.add(i);
}
}
Collections.sort(list);
Collections.reverse(list);
System.out.println(list.get(k - 1));
}
}