import java.util.*;
public class Main {
public static void main(String[] args) throws Exception {
Scanner sc = new Scanner(System.in);
int k = sc.nextInt();
int s = sc.nextInt();
int ans=0;
for(int i=0; i<=k; i++){
for(int j=0; j<=k; j++){
int z = s-i-j;
if(z >=0 && z<=k) {
ans++;
}
}
}
System.out.println(ans);
}
}