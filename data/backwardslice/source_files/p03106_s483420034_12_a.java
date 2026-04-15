import java.util.ArrayList;
import java.util.Scanner;
public class Main {
public static void main(String[] args) throws Exception {
Scanner sc = new Scanner(System.in);
int a = sc.nextInt();
int b = sc.nextInt();
int k = sc.nextInt();
sc.close();
ArrayList<Integer> list = new ArrayList<>();
for (int i = 1; i <= 100; i++) {
if (a % i == 0 && b % i == 0) {
list.add(i);
}
}
System.out.println(list.get(list.size() - k));
}
}