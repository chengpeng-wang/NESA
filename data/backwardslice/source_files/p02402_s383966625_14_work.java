import java.util.Scanner;
public class Main{
public static void main(String [] args){
Scanner sc = new Scanner(System.in);
long n,min = 0,max = 0,sum = 0,work;
n = sc.nextInt();
min = max = sum = work = sc.nextInt();
for(int i = 0; i < n - 1; i++){
work = sc.nextInt();
if(work > max){
max = work;
}
if(work < min){
min = work;
}
sum += work;
}
System.out.println(min + " " + max + " " + sum);
}
}