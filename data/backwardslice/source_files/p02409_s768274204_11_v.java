import java.util.Scanner;
public class Main {
public static void main(String[] args) {
Scanner sc = new Scanner(System.in);
int[][][] a = new int[4][3][10];
int n = sc.nextInt();
for (int i = 0; i < n; i++) {
int b = sc.nextInt();
int f = sc.nextInt();
int r = sc.nextInt();
int v = sc.nextInt();
a[b - 1][f - 1][r - 1] += v;
}
printHouse(a);
sc.close();
}
static void printHouse(int[][][] a) {
for (int i = 0; i < 4; i++) {
for (int j = 0; j < 3; j++) {
for (int k = 0; k < 10; k++) {
System.out.print(" " + a[i][j][k]);
}
System.out.print("\n");
}
if (i != 3) {
for (int l = 0; l < 20; l++) {
System.out.print("#");
}
System.out.print("\n");
}
}
}
}