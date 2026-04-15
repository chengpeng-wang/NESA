import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Scanner;
public class Main {
public static void main(String[] args) throws Exception {
Scanner sc = new Scanner(System.in);
int n = sc.nextInt();
int[] a = new int[n];
for (int i = 0; i < n; i++) {
a[i] = sc.nextInt();
}
sc.close();
int[] b = Arrays.copyOf(a, n);
Arrays.sort(b);
PrintWriter pw = new PrintWriter(System.out);
for (int i = 0; i < n; i++) {
if (a[i] == b[n - 1]) {
pw.println(b[n - 2]);
} else {
pw.println(b[n - 1]);
}
}
pw.flush();
}
}