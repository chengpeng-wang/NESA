import java.util.Scanner;
public class Main{
public static void main(String[] args) {
Scanner sc = new Scanner(System.in);
String S1 = sc.nextLine();
char[] a = S1.toCharArray();
String S2 = sc.nextLine();
char[] b = S2.toCharArray();
int number = 0;
if(a[0]==b[0]) {
number++;
}
if(a[1]==b[1]) {
number++;
}
if(a[2]==b[2]) {
number++;
}
System.out.println(number);
}
}