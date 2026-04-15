import java.util.Scanner;
public class Main {
public static void main(String[] args) {
Scanner scanner = new Scanner(System.in);
int n = scanner.nextInt();
int m = scanner.nextInt();
int count = 0;
int[] array = new int[m + n];
for (int a = 0; a < n; a++) {
array[a] = 0;
}
for (int b = n; b < m + n; b++) {
array[b] = 1;
}
for (int i = 0; i < m + n; i++) {
for (int j = i; j < m + n; j++) {
if (!(i == j) && (array[i] + array[j]) % 2 == 0) {
count++;
}
}
}
System.out.println(count);
}
}