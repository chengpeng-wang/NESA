import java.util.Scanner;
public class Main {
public static void main(String[] args) {
Scanner sc = new Scanner(System.in);
long N = sc.nextInt();
String FizzBuzz = "";
long SUM = 0;
for(int i = 1; i <= N; i++) {
if(i % 3 ==0 && i % 5 ==0) {
FizzBuzz = "FizzBuzz";
}
else if(i % 3 ==0) {
FizzBuzz = "FiZZ";
}else if(i % 5 ==0) {
FizzBuzz = "Buzz";
}
if(i%3 !=0 && i % 5 !=0) {
SUM +=i;
}
}
System.out.println(SUM);
sc.close();
}
}