import java.util.*;
import java.io.*;
public class Main {
public static void main(String[] args) {
Scanner sc = new Scanner(System.in);
int n = sc.nextInt();
int k = sc.nextInt();
long count = 0;
long[] H = new long[n];
for (int i = 0; i < n; i++) {
H[i] = sc.nextLong();
}
Arrays.sort(H);
if (n <= k) {
count = 0;
} else {
for (int i = n - 1 - k; i >= 0; i--) {
count += H[i];
}
}
System.out.println(count);
}
}