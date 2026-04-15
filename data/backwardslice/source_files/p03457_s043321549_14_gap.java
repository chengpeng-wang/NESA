import java.util.Scanner;
public class Main {
public static void main(String[] args) {
Scanner reader = new Scanner(System.in);
int N = reader.nextInt();
String ans = "Yes";
int t = 0;
int x = 0;
int y = 0;
for (int i = 0; i < N; i++) {
t = reader.nextInt() - t;
x = Math.abs(reader.nextInt() - x);
y = Math.abs(reader.nextInt() - y);
int gap = x + y;
if (gap > t || gap % 2 != t % 2) {
ans = "No";
}
}
System.out.println(ans);
reader.close();
}
}