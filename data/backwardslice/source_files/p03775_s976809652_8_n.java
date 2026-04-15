import java.util.Scanner;
public class Main{
public static void main(String[] args){
Scanner sc = new Scanner(System.in);
long n = sc.nextLong();
int ans = 11;
for(int i = 1; i <= Math.sqrt(n); i++){
if(n%i == 0){
long j= n/i;
int temp = Math.max(String.valueOf(i).length(),String.valueOf(j).length());
if(temp < ans){
ans = temp;
}
}
}
System.out.println(ans);
}
}