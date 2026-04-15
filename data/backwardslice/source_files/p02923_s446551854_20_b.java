import java.util.*;
public class Main{
public static void main(String[] args){
Scanner sc = new Scanner(System.in);
int n = sc.nextInt();
int count = 0;
int max = 0;
long a = sc.nextLong();
long b ;
for(int i=0; i<n-1; i++){
b = sc.nextLong();
if(a>=b){
count += 1;
if(max<count){
max = count;
}
}else{
count = 0;
}
a = b;
}
System.out.println(max);
}
}