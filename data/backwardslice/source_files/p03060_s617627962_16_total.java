import java.util.Scanner;
import java.util.ArrayList;
import java.util.Collections;
public class Main {
public static void main(String[] args) {
Scanner sc = new Scanner(System.in);
int N = sc.nextInt();
int total = 0;
ArrayList<Integer> list1 = new ArrayList<Integer>();
for (int i = 0; i < N; i++) {
list1.add(sc.nextInt());
}
for (int i = 0; i < N; i++) {
int A = sc.nextInt();
if (list1.get(i) > A) {
total += list1.get(i) - A;
}
}
System.out.println(total);
}
}