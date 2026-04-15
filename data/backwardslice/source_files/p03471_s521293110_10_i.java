import java.util.Scanner;
public class Main {
public static void main(String[] args) {
Scanner sc = new Scanner(System.in);
int N = sc.nextInt();
int Y = sc.nextInt();
int res10000 = -1, res5000 = -1, res1000 = -1;
for(int i = 0; i <= N; i++) {
for(int j = 0; j + i <= N; j++) {
int k = N - i - j;
int total = 10000*i + 5000*j + 1000*k;
if(total == Y) {
res10000 = i;
res5000 = j;
res1000 = k;
}
}
}
System.out.println(res10000 + " " + res5000 + " " + res1000);
}
}