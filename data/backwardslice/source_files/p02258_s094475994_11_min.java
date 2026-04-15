import java.util.Scanner;
public class Main {
public static void main(String[] args) {
Scanner sc = new Scanner(System.in);
int n = sc.nextInt();
long dMax = -1000000001;
long min = sc.nextLong();
for (int i = 1; i < n; i++) {
long r = sc.nextLong();
dMax = Math.max((r - min), dMax);
min = Math.min(r, min);
}
System.out.println(dMax);
sc.close();
}
}