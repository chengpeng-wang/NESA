import java.util.Scanner;
public class Main {
public static void main(String[] args) {
Scanner sc = new Scanner(System.in);
int n = sc.nextInt();
int k = sc.nextInt();
int[] count = new int[n];
for(int i = 0;i < n;i++) {
count[i] = 0;
}
for(int j = 0;j < k;j++) {
int[] d = new int[k];
d[j] = sc.nextInt();
int[] a = new int[d[j]];
for(int i = 0;i < d[j];i++) {
a[i] = sc.nextInt();
for(int q = 0;q < n;q++) {
if(a[i] == q+1) {
count[q] = 1;
}
}
}
}
int totalcount = 0;
for(int i = 0;i < n;i++) {
if(count[i] == 0) {
totalcount++;
}
}
System.out.println(totalcount);
}
}