import java.util.Scanner;
public class Main{
public static void main(String[] args){
Scanner sc = new Scanner(System.in);
int A = sc.nextInt();
int B = sc.nextInt();
int C = sc.nextInt();
if(A==7 || B==7 || C==7){
if(A==7){
if(B==5 && C==5){
System.out.println("YES");
}else{
System.out.println("NO");
}
}else if(B==7){
if(C==5 && A==5){
System.out.println("YES");
}else{
System.out.println("NO");
}
}else if(C==7){
if(A==5 && B==5){
System.out.println("YES");
}else{
System.out.println("NO");
}
}else{
}
}else{
System.out.println("NO");
}
sc.close();
}
}