import java.util.Scanner;
import java.util.Arrays;
class Main{
public static void main(String[] args) {
Scanner scn= new Scanner(System.in);
int n=scn.nextInt();
int[] a= new int[n];
for(int i=0;i<n;i++){
a[i]=scn.nextInt();
}
Arrays.sort(a);
int alice=0;
int bob=0;
for(int i=0;i<n;i++){
if(n%2==0){
if(i%2==0){
bob+=a[i];
}else{
alice+=a[i];
}
}else{
if(i%2==0){
alice+=a[i];
}else{
bob+=a[i];
}
}
}
System.out.println(alice-bob);
}
}