import java.util.Scanner;
public class Main{
public static void main(String[] args){
Scanner sc = new Scanner(System.in);
int n = sc.nextInt();
sc.close();
int ans = 0;
for(int i=1;i<=n;i++) {
int dig = (int) (Math.log10(i)+1);
if(dig%2==1) {
ans++;
}
}
System.out.println(ans);
}
}