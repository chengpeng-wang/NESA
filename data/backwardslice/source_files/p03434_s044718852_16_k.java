import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;
class Main {
public static void main(final String[] args) {
Scanner scan = new Scanner(System.in);
int N = scan.nextInt();
Integer[] a = new Integer[N];
for (int i = 0; i < N; i++) {
a[i] = scan.nextInt();
}
Arrays.sort(a, Comparator.reverseOrder());
int alice_total = 0;
int bob_total = 0;
for (int k = 0; k<N; k++) {
if (k%2 == 0) {
alice_total+=a[k];
} else {
bob_total += a[k];
}
}
System.out.println(alice_total - bob_total);
}
}