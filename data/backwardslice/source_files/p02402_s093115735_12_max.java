import java.util.*;
class Main {
public static void main(String[] args) {
Scanner stdIn = new Scanner(System.in);
int n = stdIn.nextInt();
long sum = 0;
int min = 1000000;
int max = -1000000;
for (int i=0; i<n; i++) {
int a = stdIn.nextInt();
min = Math.min(min,a);
max = Math.max(max,a);
sum += a;
}
System.out.println(min+" "+max+" "+sum);
}
}