import java.util.Scanner;
public class Main {
public static void main(String[] args) {
Scanner sc = new Scanner(System.in);
int N = sc.nextInt();
int ans = 0;
int prev = 0;
int count =0;
for (int i = 0; i < N; i++) {
int num = sc.nextInt();
if(num<=prev){
count++;
}else{
ans = Math.max(ans,count);
count=0;
}
prev = num;
}
ans = Math.max(ans,count);
System.out.println(ans);
}
}