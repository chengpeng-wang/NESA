import java.util.*;
public class Main {
public static void main(String[] args) throws Exception {
Scanner sc = new Scanner(System.in);
int r = sc.nextInt();
int c = sc.nextInt();
int[][] nums = new int[r + 1][c + 1];
for (int i = 0; i < r; i++) {
for (int j = 0; j < c; j++) {
int a = sc.nextInt();
nums[i][j] = a;
}
}
for (int i = 0; i < r + 1; i++) {
for (int j = 0; j < c + 1; j++) {
System.out.print(nums[i][j]);
if (j != c) {
System.out.print(" ");
}
if (r > i) {
nums[r][j] += nums[i][j];
nums[i][c] += nums[i][j];
}
if (j == c) {
System.out.println();
}
}
}
}
}