import java.util.Scanner;
public class Main {
public static void main(String[] args) {
Scanner sc = new Scanner(System.in);
int n = Integer.parseInt(sc.next());
int ret = 0;
int count = 0;
int prev = Integer.parseInt(sc.next());
for (int i = 0; i < n - 1; i++) {
int now = Integer.parseInt(sc.next());
if (prev >= now) {
count++;
} else {
ret = Math.max(ret, count);
count = 0;
}
prev = now;
}
ret = Math.max(ret, count);
System.out.println(ret);
}
}