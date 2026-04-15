import java.io.*;
import java.util.*;
//import java.util.stream.*;
//import java.math.*;
public class Main {
public static void main(String[] args) throws IOException {
BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
Scanner sc = new Scanner(System.in);
int k = sc.nextInt();
int s = sc.nextInt();
int count = 0;
for (int x = 0; x <= k; ++x) {
for (int y = 0; y <= k; ++y) {
int z = s - x - y;
if (0 <= z && z <= k) {
count++;
}
}
}
System.out.println(count);
bw.flush();
}
}