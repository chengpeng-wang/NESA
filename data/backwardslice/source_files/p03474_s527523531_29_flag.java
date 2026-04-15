import java.util.Scanner;
public class Main{
public static void main(String[] args){
Scanner sc = new Scanner(System.in);
int a = sc.nextInt();
int b = sc.nextInt();
sc.nextLine();
String c = sc.nextLine();
int flag = 0;
if(c.charAt(a) != '-'){
flag++;
}
String d = c.substring(0,a);
String e = c.substring(a+1,c.length());
if(a != d.length() || b != e.length()){
flag++;
System.out.println(a);
System.out.println(d.length());
System.out.println(b);
System.out.println(e.length());
}
int count =0;
for(char x: c.toCharArray()){
if(x == '-'){
count++;
}
}
if(count != 1){
flag++;
}
if(flag == 0){
System.out.println("Yes");
}else{
System.out.println("No");
}
}
}