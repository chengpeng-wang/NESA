import java.util.Scanner;
public class Main {
public static void main(final String[] args) {
Scanner sc = new Scanner(System.in);
int n = sc.nextInt();
int x = sc.nextInt();
int t = sc.nextInt();
sc.close();
if(n < x) {
prtln(t);
} else {
prtln((int)(((double)n / (double)x) + 0.5) * t);
}
}
public static <T> void prtln(T t) { System.out.println(t); }
}