import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.StringTokenizer;
public class Main {
public static void main(String[] args) throws Exception {
BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
int n = Integer.parseInt(input.readLine());
StringTokenizer tokn = new StringTokenizer(input.readLine());
int min = Integer.MAX_VALUE;
int max = -1;
for (int i = 0; i < n; i++) {
int value = Integer.parseInt(tokn.nextToken());
if (value < min) {
min = value;
}
if (value > max) {
max = value;
}
}
System.out.println(max - min);
}
}