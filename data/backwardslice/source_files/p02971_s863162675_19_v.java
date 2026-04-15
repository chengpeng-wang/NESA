import java.util.*;
import java.io.*;
import java.math.*;
public class Main {
public static void main(String[] args) throws Exception {
Scanner sc = new Scanner(System.in);
int n = sc.nextInt();
int maxi = 0;
int maxv = 0;
int seci = 0;
int secv = 0;
for(int i = 0; i < n; i++){
int v = sc.nextInt();
if(v > maxv){
secv = maxv;
seci = maxi;
maxv = v;
maxi = i;
}else if(v > secv){
secv = v;
seci = i;
}
}
for(int i = 0; i < n; i++){
if(i == maxi){
System.out.println(secv);
}else{
System.out.println(maxv);
}
}
}
}