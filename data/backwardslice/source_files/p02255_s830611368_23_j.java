import java.util.*;
public class Main{
private static final Scanner scan = new Scanner(System.in);
public static void main(String[] args){
int N = scan.nextInt();
int[] arr = new int[N];
for(int i = 0; i < N; i++){
int num = scan.nextInt();
arr[i] = num;
}
for(int i = 0; i < N; i++){
if(i != N - 1){
System.out.printf("%d ", arr[i]);
} else{
System.out.printf("%d\n", arr[i]);
}
}
for(int i = 1; i < N; i++){
int v = arr[i];
int j = i - 1;
while(j >= 0 && arr[j] > v){
arr[j + 1] = arr[j];
j--;
arr[j + 1] = v;
}
for(int k = 0; k < N; k++){
if(k != N - 1){
System.out.printf("%d ", arr[k]);
} else{
System.out.printf("%d\n", arr[k]);
}
}
}
}
}