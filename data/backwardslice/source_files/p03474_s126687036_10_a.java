import java.util.*;
public class Main {
public static void main(String[] args){
Scanner sc = new Scanner(System.in);
int a = sc.nextInt();
int b = sc.nextInt();
String s = sc.next();
boolean ebishu = true;
for(int i = 0 ; i < a + b + 1 ; i++){
if(i != a){
if(s.charAt(i) < 48 || s.charAt(i) > 58){
ebishu = false;
}
}
}
if(s.charAt(a) != '-') ebishu = false;
if(ebishu) System.out.println("Yes");
else System.out.println("No");
}
}