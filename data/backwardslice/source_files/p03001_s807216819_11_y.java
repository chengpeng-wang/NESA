import java.util.Scanner;
class Main{
public static void main(String[] args){
Scanner sc = new Scanner(System.in);
double w = sc.nextInt();
double h = sc.nextInt();
double x = sc.nextInt();
double y = sc.nextInt();
int b = 0;
double a = w * h / 2;
if(w / 2 == x && h / 2 == y){
b = 1;
}
System.out.println(a + " " + b);
}
}