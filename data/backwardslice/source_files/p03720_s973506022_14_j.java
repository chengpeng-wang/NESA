import java.util.*;
public class Main{
public static void main(String[] args){
Scanner sc = new Scanner(System.in);
int n = sc.nextInt();
int m = sc.nextInt();
int[][] array = new int[m][2];
int[] count = new int[n];
for(int i = 0;i < m;i++){
array[i][0] = sc.nextInt();
array[i][1] = sc.nextInt();
}
for(int i = 0;i < m;i++){
for(int j = 1;j <= n;j++){
if(array[i][0]==j||array[i][1]==j)count[j-1]++;
}
}
for(int i = 0;i < n;i++){
System.out.println(count[i]);
}
}
}