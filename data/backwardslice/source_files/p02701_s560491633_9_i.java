import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
public class Main {
public static void main(String[] args) {
Scanner scan = new Scanner(System.in);
Set<String> gift = new HashSet<>();
int N=scan.nextInt();
for(int i = 0;i < N;i++) {
gift.add(scan.next());
}
System.out.println(gift.size());
}
}