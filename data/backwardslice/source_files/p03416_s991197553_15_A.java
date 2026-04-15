import java.util.Scanner;
public class Main {
public static void main(String[] args) {
Scanner sc = new Scanner(System.in);
int A = sc.nextInt();
int B = sc.nextInt();
sc.close();
int count = 0;
String str;
for(int i=A; i<=B; i++) {
str = ""+A;
if(str.charAt(0) == str.charAt(4) && str.charAt(1) == str.charAt(3)) {
count++;
}
A++;
}
System.out.println(count);
}
}