import java.util.*;
import java.util.Map.Entry;
class Main {
static int mod =  (int) (Math.pow(10,9)+7);
static List<ArrayList<Integer>>  list = new ArrayList<ArrayList<Integer>>();
public static void main(String[] args) {
Scanner sc = new Scanner(System.in);
int N = sc.nextInt();
int K = sc.nextInt();
long[] H = new long[N];
long sum=0;
for(int i=0;i<N;i++) {
H[i]=sc.nextLong();
}
Arrays.sort(H);
for(int i=0;i<N-K;i++) {
sum=sum+H[i];
}
System.out.println(sum);
}
}