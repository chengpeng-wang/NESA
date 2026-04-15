import java.text.ParseException;
import java.util.Scanner;
public class Main {
public static void main(String[] arges) throws ParseException {
Scanner sc = new Scanner(System.in);
String line = sc.nextLine();
int v1 = Integer.parseInt(line);
int[] vs = new int[v1];
for (int i = 0; i < v1; i++) {
String line2 = sc.nextLine();
vs[i] = Integer.parseInt(line2);
}
int sosuuc = 0;
for (int i = 0; i < v1; i++) {
int count = 0;
if (vs[i] <= 10) {
for (int j = 2; j <= vs[i] /2; j++) {
if (vs[i] % j == 0) {
count++;
if (count == 1) {
break;
}
}
}
} else {
for (int j = 2; j <= Math.sqrt(vs[i]); j++) {
if (vs[i] % j == 0) {
count++;
if (count == 1) {
break;
}
}
}
}
if (count == 0) {
sosuuc++;
}
}
System.out.println(sosuuc);
}
}