import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
public class Main {
public static void main(String[] args) throws IOException {
BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
int n = Integer.parseInt(br.readLine());
long min, max, sum;
String[] strArray = br.readLine().split(" ");
long[] longArray = new long[n];
for (int i = 0;i < n;i++) {
longArray[i] = Long.parseLong(strArray[i]);
}
min = longArray[0];
max = longArray[0];
sum = longArray[0];
for (int i = 1;i < n;i++) {
min = Math.min(min,longArray[i]);
max = Math.max(max,longArray[i]);
sum += longArray[i];
}
System.out.println(min + " " + max + " " + sum);
}
}