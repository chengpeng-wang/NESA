import java.util.Scanner;
public class Main {
public static void main(String[] args) throws Exception {
Scanner sc = new Scanner(System.in);
int N = sc.nextInt();
int[] res = new int[N + 1];
int index = 1;
for (int i = 1; i <= N; i++) {
int tmp = sc.nextInt();
res[tmp] = index;
index++;
}
for (int i = 1; i <= N; i++) {
System.out.print(res[i] + " ");
}
}
}