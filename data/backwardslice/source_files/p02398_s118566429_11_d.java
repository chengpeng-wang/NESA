import java.util.Scanner;
public class Main {
public static void main(String[] args){
int count=0;
Scanner sc =new Scanner(System.in);
int a=sc.nextInt();
int b=sc.nextInt();
int c=sc.nextInt();
for(int i=1;i*i<=c;i++){
int d=c%i;
if(d==0){
if(a<=i&&i<=b)count++;
if(i!=c/i&&a<=c/i&&c/i<=b)count++;
}
}
System.out.println(count);
}
}