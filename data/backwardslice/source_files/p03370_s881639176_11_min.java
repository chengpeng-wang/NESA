import java.util.*;
public class Main{
public static void main(String[] args){
Scanner sc = new Scanner(System.in);
int n = sc.nextInt();
int x = sc.nextInt();
int min = 100_001;
for(int i=0; i<n; i++){
int m = sc.nextInt();
x -= m;
min = Math.min(min,m);
}
n += (int)(x/min);
System.out.println(n);
}
}