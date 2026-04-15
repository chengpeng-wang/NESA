import java.util.Scanner;
public class Main {
public static void main(String[] args) {
Scanner sc = new Scanner(System.in);
int n = sc.nextInt();
int a = sc.nextInt();
int b = sc.nextInt();
int result = 0;
for(int i = 1; i <= n; i++){
int res1 = i / 10000;
int res2 = (i % 10000) / 1000;
int res3 = (i % 1000) / 100;
int res4 = (i % 100) / 10;
int res5 = i % 10;
int sum = res1 + res2 + res3 + res4 + res5;
if(sum >= a && sum <= b){
result = result + i;
}
}
System.out.println(result);
sc.close();
}
}