import java.util.*;
public class Main {
public static void main(String[] args){
Scanner sc = new Scanner(System.in);
int A = sc.nextInt();
int B = sc.nextInt();
int C = sc.nextInt();
int D = sc.nextInt();
if(A<=C && B<=C){
System.out.println("0");
}else if(A<=C && B<=D){
System.out.println(B-C);
}else if(A<=C && B>D){
System.out.println(D-C);
}else if(A>C && D<=A){
System.out.println("0");
}else if(A>C && B>=D){
System.out.println(D-A);
}else{
System.out.println(B-A);
}
}
}