import java.util.*;
public class Main{
private static final Scanner scan = new Scanner(System.in);
public static void main(String[] args){
int n = scan.nextInt();
int min = 0;
int max = 0;
long sum = 0;
for(int i = 0; i < n; i++){
int a = scan.nextInt();
sum += a;
if(i == 0){
min = a;
max = a;
}
if(min > a){
min = a;
}
if(max < a){
max = a;
}
}
System.out.printf("%d %d %d\n", min, max, sum);
}
}