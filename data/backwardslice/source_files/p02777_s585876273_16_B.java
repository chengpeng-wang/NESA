import java.util.*;
public class Main {
public static void main(String[] args){
Scanner sc = new Scanner(System.in);
String S, T, U;
S = sc.next();
T = sc.next();
int A=0, B=0;
A = sc.nextInt();
B = sc.nextInt();
U = sc.next();
if (S.compareTo(U)==0){
A--;
}
if (T.compareTo(U)==0){
B--;
}
System.out.println(A + " " + B);
}
}