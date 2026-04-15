import java.util.*;
public class Main{
public static void main(String[] args){
Scanner sc = new Scanner(System.in);
int n = Integer.parseInt(sc.next());
int x = Integer.parseInt(sc.next());
int[] m = new int[n];
int num = 0;
int mMin = 10000;
for (int i = 0; i < n; i++){
m[i] = Integer.parseInt(sc.next());
x -= m[i];
num++;
if(mMin > m[i]){
mMin = m[i];
}
}
while(x >= mMin){
num++;
x -= mMin;
}
System.out.println(num);
}
}