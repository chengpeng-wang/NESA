import java.util.Scanner;
class Main {
public static void main(String[] args){
Scanner sc = new Scanner(System.in);
int n = sc.nextInt();
if(n<1 || 10000<n){
System.exit(-1);
}
int[] list = new int[n];
int a=0;
for(int i=0; i<n; i++){
a=sc.nextInt();
if(a<-1000000 || 1000000<a){
System.exit(-1);
}
list[i]=a;
}
long min = list[0];
for(int i=1; i<n; i++){
if(min>list[i]){
min = list[i];
}
}
long max = list[0];
for(int i=1; i<n; i++){
if(max<list[i]){
max = list[i];
}
}
long sum = 0;
for(int i=0; i<n; i++){
sum = sum + list[i];
}
System.out.println(min + " " + max + " " + sum);
}
}