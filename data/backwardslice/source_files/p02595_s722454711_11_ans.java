import java.util.Scanner;
public class Main {
public static void main(String[] args) {
Scanner sc = new Scanner(System.in);
int n = sc.nextInt();
int d = sc.nextInt();
int count = 0;
for(int i=0;i<n;i++){
long p = sc.nextLong();
long q = sc.nextLong();
double ans = Math.sqrt((p*p)+(q*q));
if(ans<=d)count++;
}
System.out.println(count);
}
}