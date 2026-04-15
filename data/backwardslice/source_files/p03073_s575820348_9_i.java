import java.util.Scanner;
public class Main {
public static void main(String[] args) {
Scanner scan = new Scanner(System.in);
String str = scan.next();
int min1=0;
int min2=0;
for (int i=0 ; i<str.length();i++){
if(i%2 ==Character.getNumericValue(str.charAt(i))){
min1 +=1;
}else{
min2 +=1;
}
}
System.out.println(Math.min(min1,min2));
}
}