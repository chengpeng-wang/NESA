import java.util.Scanner;
public class Main{
public static void main(String[] args){
Scanner scan = new Scanner(System.in);
int n = Integer.valueOf(scan.next());
int[] ns = new int[n];
for(int i=0;i<n;i++) {
ns[i] = Integer.valueOf(scan.next());
}
int min = ns[0];
int max = ns[1]-ns[0];
for(int i=1;i<n;i++) {
if(max < ns[i]-min) {
max = ns[i]-min;
}
if(ns[i] < min) {
min = ns[i];
}
}
System.out.println(max);
scan.close();
}
}