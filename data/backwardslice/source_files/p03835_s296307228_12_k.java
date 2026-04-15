import java.util.Scanner;
class Main {
public static void main(String[] args) {
Scanner scanner = new Scanner(System.in);
int K = scanner.nextInt();
int S = scanner.nextInt();
scanner.close();
int combination = 0;
int k;
for(int i=0;i<=K;i++) {
for(int j=0;j<=K;j++) {
k = S-i-j;
if(i+j+k==S && i>=0 && j>=0 && k>=0 && k <= K) {
combination++;
}
}
}
System.out.println(combination);
}
}