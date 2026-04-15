import java.util.*;
public class Main {
public static void main(String[] args) {
Scanner sc = new Scanner(System.in);
int n = sc.nextInt();
int limit = sc.nextInt();
int cost;
int time;
int ans = -1;
boolean con = true;
for(int i = 0; i < n;i++){
cost = sc.nextInt();
time = sc.nextInt();
if(time <= limit){
if(ans == -1) ans = cost;
else if(cost < ans) ans = cost;
}
}
if(ans == -1) System.out.println("TLE");
else System.out.println(ans);
}
}