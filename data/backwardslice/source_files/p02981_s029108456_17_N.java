import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
public class Main {
public static void main(String[] args) throws IOException {
BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
String[] NAB = br.readLine().split(" ");
int N = Integer.parseInt(NAB[0]);
int A = Integer.parseInt(NAB[1]);
int B = Integer.parseInt(NAB[2]);
if (N * A > B) {
System.out.println(B);
}
if (N * A < B) {
System.out.println(N * A);
}
if (N * A == B) {
System.out.println(B);
}
}
}