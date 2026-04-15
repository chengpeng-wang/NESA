import java.util.*;
public class Main{
public static void main(String[] args){
Scanner sc = new Scanner(System.in);
String s = sc.next();
int leng = s.length();
String X = "x";
for(int i=1; i<leng; i++){
X = X + "x";
}
System.out.println(X);
}
}