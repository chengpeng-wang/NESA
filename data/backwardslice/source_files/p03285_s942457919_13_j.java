import java.util.Scanner;
public class Main {
public static void main(String[] args) {
Scanner sc = new Scanner(System.in);
int n = sc.nextInt();
sc.close();
if(4 > n && 7 > n) {
System.out.println("No");
System.exit(0);
}
for(int i=0; i<=30; i++) {
for(int j=0; j<20; j++) {
if((i*4) + (j*7) == n) {
System.out.println("Yes");
System.exit(0);
}
}
}
System.out.println("No");
}
}