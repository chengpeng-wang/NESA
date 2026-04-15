import java.util.Scanner;
public class Main {
public static void main(String[] args) {
Scanner sc = new Scanner(System.in);
int n = sc.nextInt();
int m = sc.nextInt();
int[] food = new int[m];
int cnt = 0;
for (int i = 0; i < n; i++) {
int k = sc.nextInt();
for (int j = 0; j < k; j++) {
int a= sc.nextInt();
a--;
food[a]++;
if (food[a] == n) cnt++;
}
}
System.out.println(cnt);
sc.close();
}
}