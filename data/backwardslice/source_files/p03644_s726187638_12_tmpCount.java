import java.util.*;
public class Main {
public static void main(String[] args) {
Scanner sc = new Scanner(System.in);
int n = sc.nextInt();
int count = 0;
int res = 0;
for (int i = 1; i <= n; i++) {
int targetNumber = i;
int tmpCount = 0;
while (targetNumber % 2 == 0) {
tmpCount++;
targetNumber /= 2;
}
if (count < tmpCount) {
res = i;
count = tmpCount;
}
}
System.out.println(n == 1 ? 1 : res);
}
}