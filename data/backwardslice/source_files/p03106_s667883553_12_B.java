import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
public class Main {
public static void main(String[] args) {
Scanner scan = new Scanner(System.in);
int A = scan.nextInt();
int B = scan.nextInt();
int K = scan.nextInt();
List<Integer> list = new ArrayList<>();
for (int i = 1; i <= A; i++) {
if ((A % i == 0) && (B % i == 0)) {
list.add(i);
}
}
System.out.println(list.get(list.size() - K));
scan.close();
}
}