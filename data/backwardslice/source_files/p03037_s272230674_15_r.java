import java.util.Scanner;
public class Main {
public static void main(String[] args) {
Scanner sc = new Scanner(System.in);
int n = sc.nextInt();
int m = sc.nextInt();
int min = 0;
int max = 1_000_000;
for (int i = 0; i < m; i++) {
int l = sc.nextInt();
int r = sc.nextInt();
if (l > min) {
min = l;
}
if (r < max) {
max = r;
}
}
sc.close();
int ans;
if (max < min) {
ans = 0;
} else {
ans = max - min + 1;
}
System.out.println(ans);
}
}