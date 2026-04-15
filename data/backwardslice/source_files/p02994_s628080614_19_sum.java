import java.io.*;
import java.lang.*;
class Main {
public static void main(String[] args) throws IOException {
BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
String[] strs = br.readLine().split(" ");
int N = Integer.parseInt(strs[0]);
int L = Integer.parseInt(strs[1]);
int sum = 0, asymptote = 0;
for (int i=1; i <= N; i++) {
if (i == 1) {
asymptote = (L+i-1) > -1 ? 999 : -999;
}
if ((L+i-1) > -1 && asymptote > -1) {
asymptote = Math.min(asymptote, (L+i-1));
} else {
asymptote = Math.max(asymptote, (L+i-1));
}
sum += (L+i-1);
}
System.out.println(sum - asymptote);
}
}