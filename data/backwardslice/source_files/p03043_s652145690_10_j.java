import java.util.*;
class Main {
public static void main(String[] args) {
Scanner sc = new Scanner(System.in);
long N = sc.nextLong();
long K = sc.nextLong();
double p=0;
for(int i=1;i<=N;i++) {
double k = 1.0/N;
for(int j=i;j<K;j=j*2){
k=k/2;
}
p=p+k;
}
System.out.println(p);
}
}