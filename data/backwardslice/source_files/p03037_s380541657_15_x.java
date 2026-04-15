import java.util.Scanner;
public class Main {
long c[];
public static void main(String[] args){
Scanner sc = new Scanner(System.in);
int N = sc.nextInt();
int M = sc.nextInt();
int min, max ;
min =1;
max =N;
for (int i =0;i<M;i++) {
int x,y;
x= sc.nextInt();
y =sc.nextInt();
if (x>min) {
min =x;
}
if (y<max) {
max =y;
}
}
if (max<min) {
System.out.println(0);
return ;
}
System.out.println(max-min+1);
}
}