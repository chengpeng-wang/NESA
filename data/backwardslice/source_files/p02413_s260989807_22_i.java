public class Main {
public static void main(String[] args){
java.util.Scanner scan = new java.util.Scanner(System.in);
int r = scan.nextInt();
int c = scan.nextInt();
int[][] arr  = new int[r][c];
int sum = 0;
int kai = 0;
for(int i = 0; i < r; i++){
sum = 0;
for(int j = 0; j < c; j++){
arr[i][j] = scan.nextInt();
System.out.print(arr[i][j] + " ");
sum += arr[i][j];
}
kai += sum;
System.out.println(sum);
}
for(int i = 0; i < c; i++){
sum = 0;
for(int j = 0; j < r; j++){
sum += arr[j][i];
}
System.out.print(sum + " ");
}
System.out.println(kai);
}
}