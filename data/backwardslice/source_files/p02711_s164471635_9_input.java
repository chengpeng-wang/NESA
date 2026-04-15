import java.util.Scanner;
public class Main {
public static void main(String[] args) {
Scanner scanner = new Scanner(System.in);
int input = scanner.nextInt();
String output = "No";
while (input >= 10){
if (input%10 == 7) output = "Yes";
input/= 10;
}
if (input==7) output = "Yes";
System.out.println(output);
}
}