import java.util.*;
class Main{
public static void main(String[] args){
Scanner sc = new Scanner(System.in);
int K = sc.nextInt();
int S = sc.nextInt();
long ans = 0;
for(int x=Math.max(0,S-2*K);x<=Math.min(K,S);x++){
int left = S-x;
ans += Math.min(K,left)-Math.max(0,left-K)+1;
}
System.out.println(ans);
}
}