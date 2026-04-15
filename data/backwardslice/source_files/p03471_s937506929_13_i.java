import java.util.*;
class Main{
public static void main(String[] args){
Scanner sc = new Scanner(System.in);
int n = sc.nextInt();
int x = sc.nextInt();
if(x == 0){
System.out.println("0 0 0");
sc.close();
System.exit(0);
}
for(int i = 0; i <= n; i++){
for(int j = 0; j <= n - i; j++){
if(10000*i + 5000*j + 1000*(n - i - j) == x){
System.out.println(i + " " + j + " " + (n - i - j));
sc.close();
System.exit(0);
}
}
}
System.out.println("-1 -1 -1");
sc.close();
}
}