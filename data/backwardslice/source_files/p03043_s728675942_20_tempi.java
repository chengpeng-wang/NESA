import java.util.Scanner;
public class Main {
static Scanner stdIn = new Scanner(System.in);
public static void main(String[] args) {
int n = stdIn.nextInt();
int k = stdIn.nextInt();
double kakuritu;
int length;
if(n<k){
length = n;
kakuritu = 0.0;
}else{
length = k;
kakuritu = ((double)1/n)*(n-k);
}
for(int i=1; i<=length; i++){
double tempkakuritu = 1;
tempkakuritu *= (double)1/n;
int tempi= i;
for(; tempi<k; tempi*=2){
tempkakuritu *= 0.5;
}
kakuritu += tempkakuritu;
}
System.out.println(kakuritu);
}
}