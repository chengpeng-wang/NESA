import java.util.Scanner;
public class Main {
public static void main(String[] args) {
Scanner scanner = new Scanner(System.in);
double N = scanner.nextDouble();
int ceil = (int)Math.ceil(Math.sqrt(N));
int min = Integer.MAX_VALUE;
for (int i = 1; i < Math.max(2, ceil+1); i++) {
if(N%i == 0){
int f = Math.max(String.valueOf(i).length(), String.format("%.0f", N/i).length());
min = Math.min(f, min);
}
}
System.out.println(min);
}
}