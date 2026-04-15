import java.util.Scanner;
public class Main {
public static void main(String[] args) {
Scanner sc = new Scanner(System.in);
int n = sc.nextInt();
int q = 0;
for (int k = 1; k <= n; k+=2){
int cnt = 0;
for (int h = 1; h <= k; h++) {
if (k % h == 0) { cnt++;}
}
if (cnt == 8) {q++;}
}
System.out.println(q);
}
}