import java.util.Scanner;
import java.util.*;
public class Main {
public static void main(String[] args) throws Exception {
Scanner sc = new Scanner(System.in);
int n = sc.nextInt();
while (n != 0) {
double sum = 0;
List<Integer> vals = new ArrayList<Integer>();
for (int i = 0; i < n; i++) {
int a = sc.nextInt();
vals.add(a);
sum += a;
}
double ave = sum/n;
sum = 0;
for (int i = 0; i < n; i++) {
sum += (vals.get(i) - ave)*(vals.get(i) - ave);
}
double a = sum/n;
a = Math.sqrt(a);
System.out.println(String.format("%.5f", a));
n = sc.nextInt();
}
}
}