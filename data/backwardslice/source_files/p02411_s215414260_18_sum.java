import java.io.IOException;
import java.util.Scanner;
class Main {
public static void main(String[] args) throws IOException {
Scanner scane = new Scanner(System.in);
int m = 0, f = 0, r = 0;
int sum = 0;
m = scane.nextInt();
f = scane.nextInt();
r = scane.nextInt();
while ((m + f + r) != -3) {
sum = m + f;
if (m == -1 || f == -1){
System.out.println("F");
}
if ((m != -1 && f != -1) &&sum >= 80) {
System.out.println("A");
} else if ((m != -1 && f != -1) &&sum < 80 && sum >= 65) {
System.out.println("B");
} else if ((m != -1 && f != -1) &&sum < 65 && sum >= 50) {
System.out.println("C");
} else if ((m != -1 && f != -1) &&sum < 50 && sum >= 30) {
if (r >= 50) {
System.out.println("C");
}else {
System.out.println("D");
}
}else if (sum <30) {
System.out.println("F");
}
m = scane.nextInt();
f = scane.nextInt();
r = scane.nextInt();
}
}
}