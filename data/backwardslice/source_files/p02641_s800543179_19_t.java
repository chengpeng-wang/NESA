import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
public class Main {
public static void main(final String[] args) {
Scanner sc = new Scanner(System.in);
int x = sc.nextInt(), n = sc.nextInt();
int[] a = new int[n];
List<Integer> list = new ArrayList<Integer>();
for (int i = 0; i < n; i++) {
list.add(sc.nextInt());
}
int r = Integer.MAX_VALUE;
int res = -1;
for (int i=-5;i<=100+5;i++) {
if (!list.contains(i)) {
int t = Math.abs(i - x);
if (r > t) {
r = t;
res = i;
}
}
}
System.out.println(res);
}
}