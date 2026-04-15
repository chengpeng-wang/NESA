import java.util.*;
public class Main {
public static void main (String[] args) {
Scanner sc = new Scanner(System.in);
int n = sc.nextInt();
int k = sc.nextInt();
double total = 0;
for (int i = 1; i <= n; i++) {
double base = 1 / (double)n;
int start = i;
while (start < k) {
base /= 2;
start *= 2;
}
total += base;
}
System.out.println(java.math.BigDecimal.valueOf(total).toPlainString());
}
}