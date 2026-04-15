import java.util.Scanner;
public class Main{
public static void main(String[] args) {
Scanner stdIn=new Scanner(System.in);
int n=stdIn.nextInt();
long sum=0;
for(int i=1;i<n+1;i++) {
if(i%3!=0&&i%5!=0) {
sum+=i;
}
}
System.out.println(sum);
}
}