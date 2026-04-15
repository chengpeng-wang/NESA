import java.util.Scanner;
import java.util.*;
public class Main{
public static void main(String[] args) {
Scanner sc = new Scanner(System.in);
int n =sc.nextInt();
int k = sc.nextInt();
int[] arr = new int[n];
for(int i = 0;i<n;i++){
arr[i]=  sc.nextInt();
}
Arrays.sort(arr);
int sum =0;
for(int i=0;i<k &&i<n;i++){
sum+=arr[i];
}
System.out.println(sum);
sc.close();
}
}