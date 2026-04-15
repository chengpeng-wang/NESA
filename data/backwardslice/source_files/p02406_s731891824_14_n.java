import java.util.Scanner;
/**
* Created 2017/06/08.
*/
public class Main {
public static void main(String[] args){
Scanner sc = new Scanner(System.in);
int n = sc.nextInt();
for(int i = 1; i <= n ; i++){
String si = String.valueOf(i);
if(i % 3 == 0 || si.contains("3") ) {
System.out.print(" " + i);
}
if(i == n){
System.out.println("");
}
}
}
}