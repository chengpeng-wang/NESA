import java.util.Scanner;
class Main{
public static void main(String[] args){
Scanner sc = new Scanner(System.in);
int n = sc.nextInt();
int m = sc.nextInt();
int[] a = new int[n];
int voteSum = 0;
for(int i = 0; i < n; i++){
a[i] = sc.nextInt();
voteSum += a[i];
}
for(int i = 0; i < n-1; i++){
for(int j = i + 1; j < n; j++){
if(a[i] < a[j]){
int box = a[i];
a[i] = a[j];
a[j] = box;
}
}
}
if(a[m - 1] < ((double)voteSum/(4 * (double)m))){
System.out.println("No");
}else{
System.out.println("Yes");
}
}
}