import java.util.Scanner;
public class Main {
public static void main(String[] args) {
Scanner stdIn = new Scanner(System.in);
int N = stdIn.nextInt();
int X = stdIn.nextInt();
int[] m = new int[N];
m[0] = stdIn.nextInt();
X -= m[0];
int min = m[0];
for(int i = 1; i < N; i++) {
m[i] = stdIn.nextInt();
X -= m[i];
if(m[i] < min) {
min = m[i];
}
}
System.out.println(N+X/min);
}
}