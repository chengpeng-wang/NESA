import java.util.*;
public class Main {
public static void main(String[] args) {
Scanner sc = new Scanner(System.in);
int n = sc.nextInt();
int[] h = new int[n];
for (int i = 0; i < n; i++) {
h[i] = sc.nextInt();
}
int count = 0;
boolean flag = false;
int sum = Arrays.stream(h).sum();
while (sum != 0) {
for (int i = 0; i < n; i++) {
if (h[i] > 0) {
if (!flag) {
flag = true;
count++;
}
h[i]--;
sum--;
} else {
flag = false;
}
}
flag = false;
}
System.out.print(count);
}
}