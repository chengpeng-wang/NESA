import java.util.*;
import java.lang.*;
public class Main {
public static void main(String[] args) {
Scanner sc = new Scanner(System.in);
int n = sc.nextInt();
int max = 0;
int maxn = 1;
for (int i = 1; i <= n; i++) {
int k = i;
int count = 0;
while (k % 2 == 0) {
count++;
k /= 2;
}
if (count > max) {
max = count;
maxn = i;
}
}
System.out.println(maxn);
}
}