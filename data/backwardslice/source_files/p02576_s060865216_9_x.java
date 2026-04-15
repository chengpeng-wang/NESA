import java.util.Scanner;
public class Main {
public static void main(String[] args) throws Exception {
Scanner sc = new Scanner(System.in);
int n = sc.nextInt();
int x = sc.nextInt();
int t = sc.nextInt();
int time;
if(n % x == 0){
time = n / x * t;
} else {
time = (n / x + 1) * t;
}
System.out.println(time);
}
}