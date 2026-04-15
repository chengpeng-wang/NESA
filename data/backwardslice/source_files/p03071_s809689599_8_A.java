import java.util.Scanner;
public class Main {
public static void main(String[] args) {
Scanner scanner = new Scanner(System.in);
int A = scanner.nextInt();
int B = scanner.nextInt();
int max = Math.max(A, B);
if(A == B){
System.out.println(max * 2);
}else {
System.out.println(max * 2 - 1);
}
}
}