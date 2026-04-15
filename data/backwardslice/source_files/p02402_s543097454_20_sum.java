import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
public class Main {
public static void main(String[] args) throws IOException {
BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
int N = Integer.parseInt(br.readLine());
StringBuilder sb = new StringBuilder();
String[] set = br.readLine().split(" ");
long[] num = new long[N];
for(int i = 0; i < N; i++){
num[i] = Long.parseLong(set[i]);
}
long min = num[0];
long max = num[0];
long sum = num[0];
for (int index = 1; index < N; index++) {
min = Math.min(min, num[index]);
max = Math.max(max, num[index]);
sum += num[index];
}
System.out.print(sb.append(min).append(" ").append(max).append(" ").append(sum).append("\n"));
}
}