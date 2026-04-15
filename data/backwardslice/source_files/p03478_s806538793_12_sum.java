import java.util.Scanner;
public class Main {
public static void main(String[] args) {
Scanner sc = new Scanner(System.in);
int N = sc.nextInt();
int A = sc.nextInt();
int B = sc.nextInt();
int result = 0;
for(int i = 1; i <= N; i++) {
int sum = 0;
for(int j = i; j > 0 ; j /= 10) {
sum += j % 10;
}
if(A <= sum && sum <= B) {
result += i;
}
}
System.out.println(result);
}
}