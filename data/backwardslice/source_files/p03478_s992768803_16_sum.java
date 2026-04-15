import java.util.Scanner;
public class Main{
public static void main(String[] args) {
Scanner scan = new Scanner(System.in);
int n=scan.nextInt();
int a=scan.nextInt();
int b=scan.nextInt();
scan.close();
int ans=0;
for(int i=1;i<=n;i++) {
String s=String.valueOf(i);
int sum=0;
for(int j=0;j<s.length();j++) {
sum += Character.getNumericValue(s.charAt(j));
}
if(a<=sum&&sum<=b) {
ans+=i;
}
}
System.out.println(ans);
}
}