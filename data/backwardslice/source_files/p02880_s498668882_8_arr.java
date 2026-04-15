import java.util.Scanner;
public class Main {
public static void main(String[] args) {
Scanner scanner = new Scanner(System.in);
boolean[] arr = new boolean[101];
for (int i = 1; i <= 9; i++) {
for (int j = i; j <= 9; j++) {
arr[i * j] = true;
}
}
if (arr[scanner.nextInt()]) {
System.out.println("Yes");
} else
System.out.println("No");
}
}