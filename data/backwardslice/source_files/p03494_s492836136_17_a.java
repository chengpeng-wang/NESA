import java.util.*;
public class Main {
public static void main(String[] args) {
Scanner sc = new Scanner(System.in);
int n = sc.nextInt();
int[] a = new int[n];
int count = 0;
for (int i = 0; i < n; i++) {
a[i] = sc.nextInt();
}
while (true) {
for (int i = 0; i < n; i++) {
if (a[i] % 2 != 0) {
System.out.println(count);
System.exit(0);
} else {
a[i] /= 2;
}
}
count++;
}
}
}