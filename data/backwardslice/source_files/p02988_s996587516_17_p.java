import java.util.*;
import java.lang.*;
import java.io.*;
public class Main {
public static void main(String[] args) {
Scanner sc = new Scanner(System.in);
int n = sc.nextInt();
int[] p = new int[n+1];
int count = 0;
for(int i = 1; i <= n; i++){
p[i] = sc.nextInt();
}
for(int i = 2; i<n; i++){
if(p[i] < p[i-1] && p[i] > p[i+1]){
count++;
}
if(p[i] > p[i-1] && p[i] < p[i+1]){
count++;
}
}
System.out.println(count);
}
}