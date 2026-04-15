import java.util.Scanner;
public class Main {
public static void main(String[] args) {
Scanner scanner = new Scanner(System.in);
int n = scanner.nextInt();
int m = scanner.nextInt();
int sumn = 0;
int i;
for (i = 0; i < n; i++ ){
sumn += i;
}
int summ = 0;
int j;
for (j = 0; j < m; j++){
summ += j;
}
System.out.println(sumn + summ);
}
}