import java.util.*;
public class Main {
public static void main(String[] args){
Scanner sc = new Scanner(System.in);
int N  = sc.nextInt();
ArrayList<Integer> List = new ArrayList<>();
int sum=0,h_num=0;
for(int a=0;a<N;a++) {
List.add(sc.nextInt());
sum+=List.get(a);
}
h_num+=List.get(0);
for(int a=1;a<N;a++){
int num2=0;
if(List.get(a-1)<List.get(a))num2 = List.get(a) - List.get(a-1);
h_num+=num2;
}
System.out.println(h_num);
}
}