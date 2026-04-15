import java.util.Scanner;
public class Main {
public static void main(String[] args) {
Scanner sc = new Scanner(System.in);
int n = sc.nextInt();
int m = sc.nextInt();
int l = sc.nextInt();
int[][] list1 = new int[n][m];
for (int i = 0; i < n; i++) {
for (int j = 0; j < m; j++) {
list1[i][j] = sc.nextInt();
}
}
int[][] list2 = new int[m][l];
for (int i = 0; i < m; i++) {
for (int j = 0; j < l; j++) {
list2[i][j] = sc.nextInt();
}
}
for (int i = 0; i < n; i++) {
for (int j = 0; j < l; j++) {
long sum = 0;
for (int k = 0; k < m; k++) {
sum += list1[i][k] * list2[k][j];
}
if (j == 0) {
System.out.print(sum);
} else {
System.out.print(" " + sum);
}
}
System.out.println();
}
}
}