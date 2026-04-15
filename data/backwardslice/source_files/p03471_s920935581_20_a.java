import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.stream.Stream;
public class Main {
public static void main(String[] args) throws Exception {
BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
String s = br.readLine();
String[] sa = s.split(" ");
int n = Integer.parseInt(sa[0]);
int y = Integer.parseInt(sa[1]);
int yukichi = -1;
int ichiyou = -1;
int hideyo = -1;
for (int a = 0; a <= n; a++) {
for (int b = 0; b <= n -a; b++) {
int c = n - a - b;
int total = 10000*a + 5000*b + 1000*c;
if (total == y) {
yukichi = a;
ichiyou = b;
hideyo = c;
}
}
}
out.println(yukichi + " " + ichiyou + " " + hideyo);
out.close();
}
}