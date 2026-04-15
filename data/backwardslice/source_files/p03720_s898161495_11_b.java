import java.util.*;
public class Main{
public static void main(String[] args){
Scanner sc = new Scanner(System.in);
int n = sc.nextInt();
int m = sc.nextInt();
int[] city = new int[n];
int a, b;
for(int i = 0; i < m; i++){
a = sc.nextInt();
b = sc.nextInt();
city[a-1]++;
city[b-1]++;
}
for(int i = 0; i < n; i++){
System.out.println(city[i]);
}
}
}