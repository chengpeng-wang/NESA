import java.util.*;
public class Main {
public static void main(String[] args) {
Scanner sc = new Scanner(System.in);
int n = sc.nextInt();
int y = sc.nextInt();
int a = -1;
int b = -1;
int c = -1;
for(int i=0;i<=n;i++) {
for(int j=0;j<=n-i;j++) {
if( 10000*i + 5000*j + 1000*(n-i-j) == y ) {
a = i;
b = j;
c = n-i-j;
}
}
}
System.out.println(a + " " + b + " " + c);
}
}