import java.util.Scanner;
public class Main { 
public static void main(String[] args) {
Scanner sc = new Scanner(System.in);
String[] line = sc.nextLine().split(" ");
int firstNumber = Integer.parseInt(line[0]);
int secoundNumber = Integer.parseInt(line[1]);
int result =0;
int smallerNumber = Math.max(firstNumber, secoundNumber);
result += smallerNumber;
if(smallerNumber == firstNumber) {
firstNumber += -1;
}else {
secoundNumber += -1;
}
int secoundSmaller = Math.max(firstNumber, secoundNumber);
result += secoundSmaller;
System.out.println(result);
}
}