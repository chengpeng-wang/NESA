import java.util.*;
public class Main{
public static void main(String[] args){
Scanner sc = new Scanner(System.in);
int A = sc.nextInt();
int B = sc.nextInt();
int C = sc.nextInt();
int D = sc.nextInt();
sc.close();
int result = 0;
for(int i = 0; i < D; i++){
if (A > 0){
result += 1;
A -= 1;
} else if ( B > 0){
B -= 1;
} else {
result -= 1;
C -= 1;
}
}
System.out.println(result);
} 
}