import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
public class Main {
public static void main(String[] args) {
Scanner scanner = new Scanner(System.in);
int K = scanner.nextInt();
int A = scanner.nextInt();
int B = scanner.nextInt();
List<Integer> X = new ArrayList<Integer>();
List<Integer> Y = new ArrayList<Integer>();
for(int i = 0; i*K <= B; i++) {
if(i*K >= A) {
X.add(i*K);
}else {
Y.add(i*K);
}
}
if(X.size()==0) {
System.out.println("NG");
}else {
System.out.println("OK");
}
}
}