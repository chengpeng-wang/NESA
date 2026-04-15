import java.util.Scanner;
public class Main {
public static void main(String[] args) {
Scanner sc = new Scanner(System.in);
Long N = Long.parseLong(sc.next());
sc.close();
Long n = 0L;
for(int i = (int)Math.sqrt(N);i > 0;i--) {
if(N%i==0) {
n = N/ i;
break;
}
}
System.out.println(n.toString().length());
}
}