import java.util.Scanner;
public class Main { 
public static void main(String[] args) {
Scanner sc = new Scanner(System.in);
String[] firstLine = sc.nextLine().split(" ");
int firstNumber = Integer.parseInt(firstLine[0]);
int secoundNumber = Integer.parseInt(firstLine[1]);
String result = "Odd";
if(firstNumber%2 == 0 || secoundNumber%2 == 0) {
result = "Even";
}
System.out.print(result);
}
}