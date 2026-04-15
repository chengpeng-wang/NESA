import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
public class Main {
public static void main(String[] args) throws IOException {
BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
int n = Integer.parseInt(in.readLine());
long[] a = new long[n];
long sum = 1;
long lastSum = 1;
boolean over = false;
String[] str = in.readLine().split(" ");
for(int i = 0; i < n; i++){
a[i] = Long.parseLong(str[i]);
if(a[i]==0) {
System.out.println(0);
return;
}
sum *= a[i];
if(lastSum > sum || sum > 1000000000000000000L) {
over = true;
}
lastSum = sum;
}
System.out.println(over?-1:sum);
}
}