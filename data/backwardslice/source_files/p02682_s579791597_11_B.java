import java.util.Scanner;
public class Main {
public static void main(String[] args) throws Exception {
Scanner sc = new Scanner(System.in);
int A = sc.nextInt();
int B = sc.nextInt();
int C = sc.nextInt();
int K = sc.nextInt();
if (A >= K) {
System.out.println(K);
} else if (A + B >= K) {
System.out.println(A);
} else if (A + B + C >= K) {
System.out.println(A - (C - ((A + B + C) - K)));
}
}
}