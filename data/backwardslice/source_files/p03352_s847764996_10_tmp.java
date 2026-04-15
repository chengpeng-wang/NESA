import java.util.Scanner;
public class Main {
public static void main (String[] args) {
Scanner sc = new Scanner(System.in);
int X = sc.nextInt();
int ans = 0;
for (int i = 1; i < 33; i++){
int tmp = i;
for (int j = 0; j < 9; j++){
tmp *= i;
if (ans < tmp && tmp <= X){
ans = tmp;
}
}
}
System.out.println(ans);
}
}