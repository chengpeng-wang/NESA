import java.util.*;
import java.lang.*;
public class Main {
public static void main(String[] args) {
Scanner sc = new Scanner(System.in);
int n = Integer.parseInt(sc.next());
int[] a = new int[n];
for (int i = 0; i < n; i++) {
a[i] = Integer.parseInt(sc.next());
}
boolean finish = false;
int cnt = 0;
while (!finish) {
cnt++;
for (int i = 0; i < n; i++) {
if (a[i] % 2 != 0) {
finish = true;
cnt--;
break;
} else {
a[i] = a[i] / 2;
}
}
}
System.out.println(cnt);
}
}