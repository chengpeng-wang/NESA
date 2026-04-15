import java.util.*;
public class Main{
public static void main(String[] args){
Scanner sc = new Scanner(System.in);
int k = sc.nextInt();
int s = sc.nextInt();
int count = 0;
int z = 0;
for(int i = 0;i <= k && i <= s;i++){
for(int j = 0;j <= k && j <= s-i;j++){
z = s-i-j;
if(z >= 0&&z <= k)count++;
}
}
System.out.println(count);
}
}