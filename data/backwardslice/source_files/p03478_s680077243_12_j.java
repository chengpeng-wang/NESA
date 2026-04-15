import java.util.Scanner;
public class Main {
public static void main(String[] args) {
Scanner scan=new Scanner(System.in);
int a=scan.nextInt();
int b=scan.nextInt();
int c=scan.nextInt();
int count=0;
for(int i=0;i!=a+1;i++) {
int C=0;
for(int j=i;j!=0;j/=10) {
C+=j%10;
}
if(C>=b&&C<=c) {
count+=i;
}
}
System.out.println(count);
}
}