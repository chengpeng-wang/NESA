import java.util.*;
public class Main {
public static void main(String[] args){
Scanner sc = new Scanner(System.in);
int k = sc.nextInt();
sc.close();
int yono = 0;
for(int a = 1 ; a <= k ; a++){
for(int b = 1 ; b <= k ; b++){
for(int c = 1 ; c <= k ; c++){
int s = 1;
for(int q = 1 ; q <= Math.min(a , Math.min(b , c)) ; q++){
if(a % q == 0 && b % q == 0 && c % q == 0){
s = q;
}
}
yono += s;
}
}
}
System.out.println(yono);
}
}