import java.util.Scanner;
public class Main{
public static void main(String[] args) {
Scanner sc = new Scanner(System.in);
int a = sc.nextInt();
int b = sc.nextInt();
int c = sc.nextInt();
int[] d = new int[b];
int[][] e = new int[a][b];
int[] sum = new int[a];
int count = 0;
for (int i = 0; i < b; i++) {
d[i] = sc.nextInt();
}
for (int j = 0; j < a; j++) {
for (int i = 0; i < b; i++) {
e[j][i] = sc.nextInt();
sum[j] += e[j][i] * d[i];
}
if (sum[j] + c > 0) {
count++;
}
}
System.out.println(count);
}
}