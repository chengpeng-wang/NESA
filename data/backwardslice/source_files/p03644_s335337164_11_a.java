import java.util.Scanner;
public class Main {
public static void main(String[] args) {
Scanner stdIn = new Scanner(System.in);
int N = stdIn.nextInt();
int maxcount = 0;
int ans = 1;
for(int i = 1; i <= N; i++) {
int a = i;
int count = 0;
while(a%2 == 0 && i >= 1) {
a /= 2;
count++;
}
if(count > maxcount) {
maxcount = count;
ans = i;
}
}
System.out.println(ans);
}
}