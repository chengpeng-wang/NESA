import java.util.Scanner;
public class Main {
public static void main(String[] args) {
Scanner scan = new Scanner(System.in);
int maxNum = scan.nextInt();
int sum = scan.nextInt();
int ans = 0;
for(int x = 0; x <= maxNum; ++x){
for(int y = 0; y <= maxNum; ++y){
int z = sum - x - y;
if(0 <= z && z <= maxNum){
ans = ans + 1;
}
}
}
System.out.println(ans);
}
}