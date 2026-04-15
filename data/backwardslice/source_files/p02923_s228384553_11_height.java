import java.util.Scanner;
public class Main {
public static void main(final String[] args) {
final Scanner scanner = new Scanner(System.in);
final int n = scanner.nextInt();
int former = scanner.nextInt();
int answer = 0;
int counter = 0;
for (int i = 1; i < n; i++) {
final int height = scanner.nextInt();
if (height <= former) {
counter++;
} else {
if (counter > answer) {
answer = counter;
}
counter = 0;
}
former = height;
}
System.out.println(Math.max(counter, answer));
}
}