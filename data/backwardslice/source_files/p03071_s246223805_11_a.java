import java.util.*;
public class Main {
public static void main(String[] args) throws Exception {
Scanner sc = new Scanner(System.in);
int a = Integer.parseInt(sc.next());
int b = Integer.parseInt(sc.next());
int coin = 0;
for (int i = 0; i < 2; i++) {
if (a > b) {
coin += a;
a--;
} else {
coin += b;
b--;
}
}
System.out.println(coin);
}
}