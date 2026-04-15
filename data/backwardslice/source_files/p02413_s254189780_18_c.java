import java.util.Scanner;
public class Main{
public static void main(String[] args){
Scanner sc = new Scanner(System.in);
int r = sc.nextInt();
int c = sc.nextInt();
int[][] array = new int[r + 1][c + 1];
for(int i = 0; i < r; i++){
for(int j = 0; j < c; j++){
array[i][j] = sc.nextInt();
array[i][c] += array[i][j];
array[r][j] += array[i][j];
}
array[r][c] += array[i][c];
}
for(int i = 0; i < r + 1; i++){
System.out.print(array[i][0]);
for(int j = 1; j < c + 1; j++){
System.out.print(" " + array[i][j]);
}
System.out.println();
}
}
}