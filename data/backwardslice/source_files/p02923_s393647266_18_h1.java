import java.util.Scanner;
public class Main {
public static void main(String[] args) {
Scanner sc = new Scanner(System.in);
int n = sc.nextInt();
int jump = 0;
int max = 0;
int h1 = sc.nextInt();
int h2;
for(int i=0; i<n-1; i++) {
h2 = sc.nextInt();
if(h1 >= h2) {
jump++;
} else {
max = Math.max(max, jump);
jump = 0;
}
h1 = h2;
}
max = Math.max(max, jump);
System.out.println(max);
}
}