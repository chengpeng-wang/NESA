import java.util.*;
public class Main {
public static void main(String[] args){
Scanner sc = new Scanner(System.in);
int N = sc.nextInt();
int X = sc.nextInt();
int T = sc.nextInt();
int res;
if(N%X==0){
res = N*T/X;
}else{
res = T*(N/X+1);
}
System.out.println(res);
}
}