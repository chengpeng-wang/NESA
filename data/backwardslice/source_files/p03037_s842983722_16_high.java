import java.util.*;
public class Main{
public static void main(String[] args){
Scanner scan = new Scanner(System.in);
int n = scan.nextInt();
int m = scan.nextInt();
int lower = 0;
int upper = n+1;
for (int j=0;j<m;j++){
int low = scan.nextInt();
int high = scan.nextInt();
if (lower < low){
lower = low;
}
if (high < upper){
upper = high;
}
}
if (lower > upper){
System.out.println(0);
return;
}
System.out.println(upper-lower+1);
}
}