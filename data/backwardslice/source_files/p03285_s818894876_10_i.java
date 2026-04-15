import java.util.*;
public class Main {
public static void main(String[] args) {
Scanner sc = new Scanner(System.in);
int N = sc.nextInt();
sc.close();
boolean can = false;
for (int i = 0; i < 25; ++i) {
for (int j = 0; j < 25; ++j) {
if (N == 4 * i + 7 * j) {
can = true;
}
}
}
if (can == true) {
System.out.print("Yes");
} else {
System.out.print("No");
}
}
}