import java.util.Scanner;
public class Main {
public static void main(String[] args) {
Scanner sc = new Scanner(System.in);
int n = sc.nextInt();
int a[] = new int[n];
for (int i = 0; i < n; i++) {
a[i] = sc.nextInt();
}
for (int k = 0; k < n; k++) {
if (k != n - 1) {
System.out.print(a[k]);
System.out.print(" ");
} else {
System.out.println(a[k]);
}
}
insertionSort(n, a);
}
public static void insertionSort(int n, int[] a) {
int j, v;
for (int i = 1; i < n; i++) {
v = a[i];
j = i - 1;
while (j >= 0 && a[j] > v) {
a[j + 1] = a[j];
j--;
}
a[j + 1] = v;
for (int k = 0; k < n; k++) {
if (k != n - 1) {
System.out.print(a[k]);
System.out.print(" ");
} else {
System.out.println(a[k]);
}
}
}
}
}