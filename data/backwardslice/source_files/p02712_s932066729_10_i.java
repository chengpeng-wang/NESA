import java.util.Arrays;
import java.util.Scanner;
public class Main{
public static Scanner scan  = new Scanner(System.in);
public static void main(String[]args){
long n;
n = nextLong();
Long sum = 0L;
for(long i = 1;i<=n;i++){
if(i%3!=0&&i%5!=0&&i%15!=0){
sum+=i;
}
}
print(sum);
}
public static int nextInt(){
return Integer.parseInt(scan.next());
}
public static long nextLong(){
return Long.parseLong(scan.next());
}
public static String next(){
return scan.next();
}
public static double nextDouble(){
return Double.parseDouble(scan.next());
}
public static float nextFloat(){
return Float.parseFloat(scan.next());
}
//Yes or No
public static void yesNo(boolean flag){
if(flag) System.out.println("Yes");
else System.out.println("No");
}
public static void print(Object a){
System.out.println(a);
}
}