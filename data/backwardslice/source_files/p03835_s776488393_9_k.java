import java.util.*;
public class Main{
public static void main(String[] args){
Scanner sc = new Scanner(System.in);
int k = sc.nextInt();
int s = sc.nextInt();
int num = 0;
for(int x=0; x<=Math.min(k, s); x++){
for(int y=0; y<=k; y++){
if(0<=s-x-y && s-x-y<=k)num++;
}
}
System.out.println(num);
}
}