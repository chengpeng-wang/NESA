import java.util.*;
public class Main {
public static void main(String[] args) {
Scanner sc = new Scanner(System.in);
String s = sc.next();
int A = Character.getNumericValue(s.charAt(0));
int B = Character.getNumericValue(s.charAt(1));
int C = Character.getNumericValue(s.charAt(2));
int D = Character.getNumericValue(s.charAt(3));
if(A+B+C+D==7){
System.out.println(A+"+"+B+"+"+C+"+"+D+"=7");
}else if(A-B+C+D==7){
System.out.println(A+"-"+B+"+"+C+"+"+D+"=7");
}else if(A+B-C+D==7){
System.out.println(A+"+"+B+"-"+C+"+"+D+"=7");
}else if(A+B+C-D==7){
System.out.println(A+"+"+B+"+"+C+"-"+D+"=7");
}else if(A-B-C+D==7){
System.out.println(A+"-"+B+"-"+C+"+"+D+"=7");
}else if(A-B+C-D==7){
System.out.println(A+"-"+B+"+"+C+"-"+D+"=7");
}else if(A+B-C-D==7){
System.out.println(A+"+"+B+"-"+C+"-"+D+"=7");
}else if(A-B-C-D==7){
System.out.println(A+"-"+B+"-"+C+"-"+D+"=7");
}
}
}