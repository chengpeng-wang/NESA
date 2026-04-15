import java.util.*;
public class Main {
public static void main(String[] args) {
Scanner sc = new Scanner(System.in);
String n = String.valueOf(sc.nextInt());
String rn = "",rs = "";
char n1 = n.charAt(0),n2 = n.charAt(1),n3 = n.charAt(2);
if(n1 == n2 && n2 == n3) {
System.out.println(Integer.valueOf(n));
return;
}else if(n1 == n2){
if((int)n2 > (int)n3) {
rs = String.valueOf(n1);
}else {
rs = String.valueOf((char)((int)n1 + 1));
}
}else {
if((int)n1 > (int)n2) {
rs = String.valueOf(n1);
}else {
rs = String.valueOf((char)((int)n1 + 1));
}
}
rn += rs+rs+rs;
System.out.println(rn);
}
}