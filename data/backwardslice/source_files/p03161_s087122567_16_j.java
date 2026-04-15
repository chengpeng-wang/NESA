import java.util.*;
public class Main {
public static void main (String[] args) {
Scanner sc = new Scanner(System.in);
int t = sc.nextInt();
int k = sc.nextInt();
int[] arr = new int[t];
for(int i =0;i<t;i++){
arr[i]= sc.nextInt();
}
int[] dp = new int[t];
dp[0] = 0;
for(int i =1;i<t;i++){
int min = Integer.MAX_VALUE;
for(int j =1;j<=k;j++){
if(i-j>=0){
min = Math.min(dp[i-j]+ Math.abs(arr[i]-arr[i-j]),min);
}
}
dp[i]= min;
}
System.out.println(dp[dp.length-1]);
}
}