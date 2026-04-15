import java.util.Scanner;
public class Main {
public static void main(String[] args) {
Scanner sc = new Scanner(System.in);
String line = sc.nextLine();
int strLength = line.length();
StringBuilder sb = new StringBuilder();
for(int i = 0; i<strLength; i++) {
sb.append('x');
}
System.out.println(sb.toString());
}
}