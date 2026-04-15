import java.util.Scanner;
public class Main {
public static void main(String[] args) {
Scanner sc = new Scanner(System.in);
int n = sc.nextInt();
int a = sc.nextInt();
int b = sc.nextInt();
sc.close();
int tmpSum = 0;
int sum = 0;
for(int i=0; i<=n; i++) {
String s = Integer.toString(i);
for(int j=0; j<s.length(); j++) {
tmpSum += Character.getNumericValue(s.charAt(j));
}
if(tmpSum >= a && tmpSum <= b) {
sum += i;
}
tmpSum = 0;
}
System.out.println(sum);
}
}