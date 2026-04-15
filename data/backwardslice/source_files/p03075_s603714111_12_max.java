import java.util.Scanner;
public class Main {
public static void main(String[] args) {
Scanner sc = new Scanner(System.in);
int min = Integer.MAX_VALUE;
int max = Integer.MIN_VALUE;
for (int i = 0; i < 5; i++) {
int tmp = sc.nextInt();
if (tmp < min) {
min = tmp;
}
if (tmp > max) {
max = tmp;
}
}
int k = sc.nextInt();
sc.close();
if (max - min > k) {
System.out.println(":(");
} else {
System.out.println("Yay!");
}
}
}