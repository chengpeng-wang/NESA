import java.util.Scanner;
import java.io.IOException;
class Main {
public static void main(String[] args) throws IOException {
Scanner scan = new Scanner(System.in);
int n = Integer.parseInt(scan.nextLine());
int big = Integer.MIN_VALUE;
int small = Integer.MAX_VALUE;
long total = 0;
for (int i = 0; i < n; i++) {
int a = scan.nextInt();
if (big < a) {
big = a;
}
if (a < small) {
small = a;
}
total = total + a;
}
System.out.println(small + " " + big + " " + total);
}
}