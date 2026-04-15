import java.util.*;
public class Main {
public static void main(String [] args){
Scanner sc = new Scanner(System.in);
int N = sc.nextInt();
int M = sc.nextInt();
int C = sc.nextInt();
int opt=0;
ArrayList<Integer> B = new ArrayList<>();
for(int a=0;a<M;a++) B.add(sc.nextInt());
for(int a=0;a<N;a++){
int num = 0;
for(int b=0;b<M;b++){
int num2 = sc.nextInt();
num+=num2*B.get(b);
}
num+=C;
if(num>0)opt++;
}
System.out.println(opt);
}
}