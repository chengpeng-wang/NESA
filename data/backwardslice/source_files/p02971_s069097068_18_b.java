import java.io.*;
import java.util.*;
public class Main {
public static void main(String[] args) throws Exception {
Scanner sc = new Scanner(System.in);
int n = sc.nextInt();
int[] a = new int[n];
int key1 = 0;
int key2 = 0;
int val1 = 0;
int val2 = 0;
for (int i = 0; i < n; i++) {
int b = sc.nextInt();
a[i] = b;
if (b > val1) {
val1 = b;
key1 = i;
} else if (b > val2) {
val2 = b;
key2 = i;
}
}
for (int i = 0; i < n; i++) {
if (a[i] == val1) {
System.out.println(val2);
} else {
System.out.println(val1);
}
}
}
}