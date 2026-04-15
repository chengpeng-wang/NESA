import java.util.Scanner;
import java.lang.Math;
public class Main{
public static void main(String[] args) {
Scanner scan = new Scanner(System.in);
int n = Integer.parseInt(scan.nextLine());
String input = scan.nextLine();
String[] inputs = input.split(" ");
int[] numbers = new int[n];
for (int i = 0; i < n; i++) {
numbers[i] = Integer.parseInt(inputs[i]);
}
int min = 1000000;
int max = -1000000;
long sum = 0;
for (int i = 0; i < n; i++) {
if (min > numbers[i]) {
min = numbers[i];
}
if (max < numbers[i]) {
max = numbers[i];
}
sum += (long)numbers[i];
}
System.out.println(min + " " + max + " " + sum);
}
}