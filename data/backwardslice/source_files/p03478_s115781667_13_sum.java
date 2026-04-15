import java.util.Scanner;
public class Main {
public static void main(String[] args) {
Scanner sc = new Scanner(System.in);
int n = sc.nextInt();
int a = sc.nextInt();
int b = sc.nextInt();
int ans = 0;
for(int i = 1; i <= n; i++) {
int temp = i;
int sum = 0;
while(temp != 0) {
sum += temp%10;
temp = temp/10;
}
if(sum >= a && sum <= b) {
ans += i;
}
}
System.out.println(ans);
}
}