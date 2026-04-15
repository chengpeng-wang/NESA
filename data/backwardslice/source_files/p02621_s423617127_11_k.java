import java.util.Scanner;
public class Main {
public static void main(String[] args) {
Scanner sc = new Scanner(System.in);
int input = Integer.parseInt(sc.nextLine());
int result = 0;
for (int i=0; i<1; i++) {
result = 1 * input;
for (int j=0; j<1; j++) {
result += input * input;
for (int k=0; k<1; k++) {
result += input * input * input;
}
}
}
System.out.println(result);
}
}