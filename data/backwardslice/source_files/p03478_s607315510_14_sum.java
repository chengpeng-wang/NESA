import java.util.Scanner;
class Main {
public static void main(String[] args) {
Scanner stdIn = new Scanner(System.in);
int ans = 0;
int n = stdIn.nextInt();
int a = stdIn.nextInt();
int b = stdIn.nextInt();
for (int i = 1; i <= n; i++) {
int sum = 0;
String s = String.valueOf(i);
String[] str = s.split("");
for (int j = 0; j < s.length(); j++) {
sum += Integer.parseInt(str[j]);
}
if (sum >= a && sum <= b) {
ans += i;
}
}
System.out.println(ans);
}
}