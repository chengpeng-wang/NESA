import java.util.Scanner;
public class Main{
public static void main(String[] args) {
Scanner input = new Scanner(System.in);
int  dig1 = input.nextInt();
int dig2 = input.nextInt();
input.close();
int prod = dig1 * dig2;
if(prod % 2 == 0 ) {
System.out.println("Even");
}else{
System.out.println("Odd");
}
}
}