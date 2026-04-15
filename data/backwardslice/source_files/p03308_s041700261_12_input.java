import java.util.Scanner;
public class Main {
public static void main(String[] args) throws Exception {
Scanner sc = new Scanner(System.in);
int N = sc.nextInt();
int max = 0;
int min = Integer.MAX_VALUE;
for (int i = 0; i < N; i++) {
int input = sc.nextInt();
if (input > max)
max = input;
if (input < min)
min = input;
}
System.out.println(max - min);
sc.close();
}
}