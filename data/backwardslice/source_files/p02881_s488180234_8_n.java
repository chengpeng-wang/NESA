import java.util.Scanner;
public class Main {
public static void main(String[] args) {
Scanner sc = new Scanner(System.in);
long n = Long.parseLong(sc.next());
long x = n;
for(int i = 1; i <= Math.sqrt(n); i++){
if(n%i == 0){
x = i;
}
}
System.out.println(x + n/x -2);
}
}