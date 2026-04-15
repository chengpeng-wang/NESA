import java.util.*;
public class Main {
public static void main(String[] args) {
Scanner sc = new Scanner(System.in);
int n = sc.nextInt();
int[] w = new int[n];
for (int i = 0; i < n; i++) {
w[i] = sc.nextInt();
}
int gap = 0;
int min = 100;
for (int t = 1; t < n; t++) {
int total1 = 0;
int total2 = 0;
for (int j = 0; j < t; j++) {
total1 += w[j];
}
for (int k = t; k < n; k++) {
total2 += w[k];
}
gap = Math.abs(total2 - total1);
min = Math.min(min,gap);
}
System.out.println(min);
}
}