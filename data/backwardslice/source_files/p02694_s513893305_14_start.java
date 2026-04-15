import java.io.PrintWriter;
import java.util.Scanner;
public class Main {
public static void main(String[] args) {
Scanner in = new Scanner(System.in);
PrintWriter out = new PrintWriter(System.out);
long x = in.nextLong();
long start = 100;
int days = 0;
while (start + start / 100l <= x) {
start += start / 100l;
days++;
}
if (start < x) {
days++;
}
out.println(days);
out.close();
}
}