import java.util.Scanner;
public class Main {
public static void main(String[] args) {
Scanner sc = new Scanner(System.in);
int K = sc.nextInt();
sc.close();
int total = 0;
for(int a = 1; a <= K; a++) {
for(int b = 1; b <= K; b++) {
for(int c = 1; c <= K; c++) {
int result = gcdThree(a , b, c);
total += result;
}
}
}
System.out.println(total);
}
static int gcd(int a, int b) {
int temp;
while((temp = a % b) != 0) {
a = b;
b = temp;
}
return b;
}
static int gcdThree(int a , int b, int c) {
int gcdTwo = gcd(a, b);
int result = gcd(gcdTwo , c);
return result;
}
}