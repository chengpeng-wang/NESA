import java.util.Scanner;
public class Main {
public static void main(String[] args) {
Scanner sc = new Scanner(System.in);
int n = sc.nextInt();
int array[] = new int[n];
for(int i = 0; i < n; i++) {
int a = sc.nextInt();
array[i] = a;
}
for(int i = 0; i < n; i++) {
System.out.print(array[(n - 1) - i]);
if(i == n - 1) {
break;
}
System.out.print(" ");
}
System.out.println();
}
}