import java.util.Scanner;
import java.util.*;
public class Main {
public static void main(String[] args) {
Scanner sc = new Scanner(System.in);
int a = sc.nextInt();
int b = sc.nextInt();
int c = sc.nextInt();
int max = a;
if (max < b)
max = b;
if (max < c)
max = c;
int min = a;
if (min > b)
min = b;
if (min > c)
min = c;
if (max == 7) {
if (min == 5) {
if (a + b + c - max - min == 5) {
System.out.println("YES");
} else {
System.out.println("NO");
}
} else {
System.out.println("NO");
}
} else {
System.out.println("NO");
}
}
}