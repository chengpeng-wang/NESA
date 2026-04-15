import java.util.Scanner;
public class Main {
public static void main(String[] args){
Scanner sc = new Scanner(System.in);
int A = sc.nextInt();
int B = sc.nextInt();
int C = sc.nextInt();
int X = sc.nextInt();
int Y = sc.nextInt();
int count = 0;
if(A+B>2*C){
if(X>=Y){
if(A>=2*C){
count = 2*C*X;
}else{
count = 2*C*Y + A*(X-Y);
}
}else{
if(B>=2*C){
count = 2*C*Y;
}else{
count = 2*C*X + B*(Y-X);
}
}
}else{
count = A*X+B*Y;
}
System.out.println(count);
}
}