import java.util.Scanner;
import java.io.IOException;
public class Main {
public static void main(String[] args) throws IOException {
Scanner sc = new Scanner(System.in);
int s = Integer.parseInt(sc.next());
int w = Integer.parseInt(sc.next());
sc.close();
String result = "safe";
if (w >= s) {
result = "unsafe";
}
System.out.println(result);
}
}