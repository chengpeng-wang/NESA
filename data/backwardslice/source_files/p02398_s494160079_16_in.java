import java.util.Scanner;
public class Main {
public static void main(String[] args) {
Scanner sc = new Scanner(System.in);
String s = sc.nextLine();
String[] st = s.split(" ");
int[] in = new int[3];
for (int i = 0; i < 3; i++) {
in[i] = Integer.parseInt(st[i]);
}
int x = 0;
while (in[0] <= in[1]) {
if (in[2] % in[0] == 0) {
x++;
}
in[0]++;
}
System.out.println(x);
}
}