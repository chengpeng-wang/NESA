import java.time.Instant;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
public class Main {
public static void main(String[] args){
Scanner sc = new Scanner(System.in);
int N = sc.nextInt();
int K = sc.nextInt();
int[] cnt = new int[N];
for(int i=0; i<N; i++){
int c = 0;
int n = i+1;
while(n<K) {
n= n*2;
c++;
}
cnt[i]=c;
}
Double ans = 0.0;
for(int i=0; i<N; i++) {
ans += (double) 1 / N * (double) (1 / Math.pow(2, cnt[i]));
}
System.out.println(ans);
}
}