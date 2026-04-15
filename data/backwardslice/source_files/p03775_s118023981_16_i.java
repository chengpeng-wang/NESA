import java.util.*;
import java.io.PrintWriter;
import static java.lang.Integer.*;
import static java.lang.Long.*;
import static java.lang.Math.*;
import static java.lang.System.*;
public class Main {
public static final int AB = 26;
public static void main(String[] args) {
long i=0,j=0,k=0;
Scanner sc = new Scanner(in);
long n = parseLong(sc.next());
sc.close();
long mn = 10;
for (i = 1; i*i <= n; i++) {
if(n%i==0) {
long tmp=(long) Math.max(floor(log10(i)+1),floor(log10(n/i)+1));
if(tmp<mn)mn=tmp;
}
}
out.println(mn);
}
}