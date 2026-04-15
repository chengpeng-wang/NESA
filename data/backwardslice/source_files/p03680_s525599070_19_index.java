import java.util.Scanner;
public class Main {
public static void main(String[] args) throws Exception {
Scanner sc = new Scanner(System.in);
int N = sc.nextInt();
int[] input = new int[N + 1];
for (int i = 1; i <= N; i++) {
int tmp = sc.nextInt();
input[i] = tmp;
}
int index = 1;
int res = 0;
while (index != 2) {
if (input[index] == 0) {
System.out.println(-1);
System.exit(0);
}
int tmp = input[index];
input[index] = 0;
index = tmp;
res++;
}
System.out.println(res);
}
}