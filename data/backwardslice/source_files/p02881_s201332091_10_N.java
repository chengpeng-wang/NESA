import java.io.PrintWriter;
import java.util.*;
public class Main {
public static void main(String[] args) {
Scanner sc = new Scanner(System.in);
PrintWriter out = new PrintWriter(System.out);
long N = Long.parseLong(sc.next());
long max = 1;
for(int i=1;i<=Math.round(Math.sqrt(N));i++) {
if(N%i==0) {
max = i;
}
}
long pair = N/max;
long result = (pair-1) + (max-1);
out.println(result);
out.flush();
}
}