import java.util.Scanner;
import java.util.ArrayList;
public class Main {
public static void main(String[] args) {
Scanner scan = new Scanner(System.in);
int N = scan.nextInt();
ArrayList<Integer> ar = new ArrayList<Integer>();
for(int i=0; i<N; i++) {
ar.add(i+1);
}
int K = scan.nextInt();
for(int i=0; i<K; i++) {
int d = scan.nextInt();
for(int j=0; j<d; j++) {
int A = scan.nextInt();
ar.remove(new Integer(A));
}
}
scan.close();
System.out.println(ar.size());
}
}