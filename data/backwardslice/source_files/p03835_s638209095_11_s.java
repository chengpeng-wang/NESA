import java.util.*;
public class Main {
public static void main(String[] args) throws Exception {
Scanner sc = new Scanner(System.in);
long[] map = new long[100002];
int k = sc.nextInt();
int s = sc.nextInt();
int ans = 0;
for(int i = 0; i <= k; i++){
for(int j = 0; j <= k; j++){
if(s-i-j >= 0 && s-i-j <= k){
ans++;
}
}
}
System.out.println(ans);
}
}