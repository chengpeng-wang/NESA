import java.util.*;
class Main {
public static void main(String[] args) {
Scanner sc = new Scanner(System.in);
int d = sc.nextInt();
int t = sc.nextInt();
int s = sc.nextInt();
if (d % s == 0) {
if (d / s <= t) {
System.out.println("Yes");
} else {
System.out.println("No");
}
} else {
if ((d / s) + 1 <= t) {
System.out.println("Yes");
} else {
System.out.println("No");
}
}
}
}