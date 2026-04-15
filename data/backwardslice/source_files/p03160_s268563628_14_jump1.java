import java.util.*;
public class Main{
public static void main(String[] args) {
Scanner sc = new Scanner(System.in);
int N = sc.nextInt();
int[] height = new int[N+1];
int[] dp = new int[N+1];
for(int i = 1; i <= N; i++) {
height[i] = sc.nextInt();
}
dp[1] = 0;
dp[2] = Math.abs(height[2]-height[1]);
for(int i = 3; i <= N; i++) {
int jump1 = Math.abs(height[i]-height[i-1]);
int jump2 = Math.abs(height[i]-height[i-2]);
dp[i] = Math.min(dp[i-1]+jump1,dp[i-2]+jump2);
}
System.out.println(dp[N]);
}
}