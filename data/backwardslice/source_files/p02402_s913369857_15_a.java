import java.util.Scanner;
public class Main {
public static void main(String[] args) {
Scanner sc = new Scanner(System.in);
int n = sc.nextInt();
int max = Integer.MIN_VALUE;
int min = Integer.MAX_VALUE;
long b = 0;
for(int i = 1;i<=n;i++){
int a = sc.nextInt();
b = b + a;
if(max<a) {
max = a;
}
if(min>a) {
min = a;
}
}
System.out.println(min+" "+max+" "+b)	;
}
}