import java.util.Scanner;
public class Main {
public static void main(String[] args) {
Scanner t=new Scanner(System.in);
int k=t.nextInt(), x=t.nextInt();
for(int i=(x-k)+1;i<x;i++) {
System.out.print(i + " ");
}
for(int i=x;i<x+k;i++) {
if(i == (x+k)-1) {
System.out.println(i);
}else {
System.out.print(i + " ");
}
}
}
}