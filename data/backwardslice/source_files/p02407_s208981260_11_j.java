import java.util.Scanner;
public class Main {
public static void main(String[] args) {
Scanner sc = new Scanner(System.in);
int a = sc.nextInt();
int[] l = new int[a];
for(int i = 0; i < a; i++) {
l[i] = sc.nextInt();
}
for(int j = a-1; j >= 0; j--) {
if(j == 0) {
System.out.println(l[j]);
} else {
System.out.print(l[j] + " ");
}
}
}
}