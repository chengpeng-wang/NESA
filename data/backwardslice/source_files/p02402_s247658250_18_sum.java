import java.util.Scanner;
public class Main{
public static void main(String[] args){
Scanner in = new Scanner(System.in);
int a = in.nextInt();
int b = in.nextInt();
int min = b;
int max = b;
long  sum = b;
for(int i = 0 ;i<a-1 ;i++){
int c = in.nextInt();
if(min>c){
min = c;
}
if(max<c){
max = c;
}
sum += c;
}
System.out.println(min+" "+max+" "+sum);
}
}