import java.util.Scanner;
public class Main {
public static void main(String[] args) {
Scanner sc = new Scanner(System.in);
int N = sc.nextInt();
sc.close();
String a = "No";
for(int i=1; i<10; i++) {
for(int j=1; j<10; j++) {
if(N == i*j) {
a = "Yes";
}
}
}
System.out.println(a);
}
}