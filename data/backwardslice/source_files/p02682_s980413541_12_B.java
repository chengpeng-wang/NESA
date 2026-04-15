import java.util.*;
class Main {
public static void main(String[] args) {
Scanner sc = new Scanner(System.in);
int A = sc.nextInt();
int B = sc.nextInt();
int C = sc.nextInt();
int K = sc.nextInt();
int ans = 0;
if (A >= K) {
ans = K;
} else if (A+B >= K){
ans = A;
} else {
ans = A - (K-(A+B));
}
System.out.println(ans);
}
}