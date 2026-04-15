import java.util.*;
public class Main {
public static void main(String[] args) {
Scanner sc = new Scanner(System.in);
int a = sc.nextInt();
int b = sc.nextInt();
int x = Math.max(a, b);
int y = Math.min(a, b);
boolean equal = true;
if(x == y) {
System.out.println(x);
equal = false;
}
x %= y;
int max = 0;
for(int i = Math.min(x, y); i > 0; i--) {
if(x % i == 0 && y % i == 0) {
max = i;
break;
}
}
if(equal) {
System.out.println(max);
}
}
}