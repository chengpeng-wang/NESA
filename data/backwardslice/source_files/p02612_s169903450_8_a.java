import java.util.*;
public class Main {
public static void main(String[] args){
Scanner scn = new Scanner(System.in);
int p = scn.nextInt();
int a = p%1000;
int ans = 1000-a;
if(a==0){
System.out.println("0");
}else{
System.out.println(ans);
}
}
}