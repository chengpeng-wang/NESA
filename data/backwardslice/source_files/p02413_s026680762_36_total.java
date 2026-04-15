import java.util.Scanner;
import java.util.ArrayList;
public class Main {
public static void main(String[] args) {
Scanner s = new Scanner(System.in);
int r = s.nextInt();
int c = s.nextInt();
int[][] table = new int[r][c];
for (int i = 0; i < r; i++) {
for (int j = 0; j < c; j++) {
table[i][j] = s.nextInt();
}
}
s.close();
for (int i = 0; i < r; i++) {
int sum = 0;
for (int j = 0; j <= c; j++) {
if (j < c) {
System.out.print(table[i][j] + " ");
sum += table[i][j];
} else {
System.out.println(sum);
}
}
}
int total = 0;
for (int j = 0; j < c; j++) {
int colSum = 0;
for (int i = 0; i <= r; i++) {
if (i < r) {
colSum += table[i][j];
} else {
System.out.print(colSum + " ");
}
}
total += colSum;
}
System.out.print(total + "\n");
}
}