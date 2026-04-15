import java.util.*;
public class Main{
public static void main(String[] args){
Scanner sc = new Scanner(System.in);
int N = sc.nextInt();
boolean sucess = true;
for(int i=0; i<N; i++) {
int t = sc.nextInt();
int x = sc.nextInt();
int y = sc.nextInt();
if ((x+y)%2 != t%2 || (x+y) > t) {
sucess = false;
break;
}
}
if (sucess) {
System.out.println("Yes");
} else {
System.out.println("No");
}
}
}