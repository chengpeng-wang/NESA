import java.util.Scanner;
public class Main {
public static void main(String[] args) {
Scanner sc = new Scanner(System.in);
int N = sc.nextInt();
int K = sc.nextInt();
double count = 0;
for(int i=1; i <= N; i++){
double tmp02 = 1;
for(int n=i; n < K; n=n*2){
tmp02 = tmp02 * 0.5;
}
count = count + tmp02 / N;
}
System.out.print(count);
}
}