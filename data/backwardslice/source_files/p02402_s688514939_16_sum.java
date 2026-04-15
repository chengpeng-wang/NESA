import java.util.*;
public class Main {
public static void main(String[] args) {
Scanner sc = new Scanner(System.in);
int n,num,min,max;
long sum = 0;
n = sc.nextInt();
num = sc.nextInt();
min = num;
max = num;
sum += num;
for(int i=1; i<n; i++) {
num = sc.nextInt();
min = Math.min(min, num);
max = Math.max(max, num);
sum += num;
}
System.out.println(min + " " + max + " " + sum);
}
}