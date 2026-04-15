import java.util.*;
import java.text.*;
/*
Main class for AOJ where there is no input.
*/
public class Main{
public static void main(String[] args){
Scanner sc = new Scanner(System.in);
int n = Integer.parseInt(sc.nextLine());
double debt = 100000;
while(n > 0){
debt *= 1.05;
debt = 1000.0*Math.ceil(debt/1000.0);
n--;
}
System.out.println(String.format("%.0f",debt));
}//main
}//class