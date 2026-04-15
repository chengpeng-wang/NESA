import java.util.Scanner;
public class Main {
public static void main(String[] args) {
Scanner scan=new Scanner(System.in);
int a=scan.nextInt();
int b=scan.nextInt();
int ans=1145148100;
for(int i=0;i!=a;i++){
int c=scan.nextInt();
int d=scan.nextInt();
if(d<=b) {
if(c<=ans) {
ans=c;
}
}
}
System.out.println(ans==1145148100?"TLE":ans);
}
}