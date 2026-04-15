import java.util.Scanner;
public class Main {
public static void main(String[] args) {
Scanner scan = new Scanner(System.in);
int n = scan.nextInt();
int a = scan.nextInt();
int b = scan.nextInt();
scan.close();
int sumResult = 0;
for(int i=1; i<=n; ++i) {
int sumPerKeta = 0;
String formated = String.format("%04d", i);
for(int j=0; j<= 3; ++j) {
char numChar = formated.charAt(j);
int num = Character.getNumericValue(numChar);
sumPerKeta += num;
}
if(a <= sumPerKeta && sumPerKeta <= b) {
sumResult += i;
}
}
System.out.println(sumResult);
}
}