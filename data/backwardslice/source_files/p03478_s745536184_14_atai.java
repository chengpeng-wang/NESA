import java.lang.*;
import java.util.*;
public class Main {
public static void main(String[] args){
Scanner sc = new Scanner(System.in);
int n = sc.nextInt();
int a = sc.nextInt();
int b = sc.nextInt();
int result = 0;
for(int i = 0;i<n;i++){
int kari = i+1;
int atai = 0;
while(kari!=0){
atai += (kari%10);
kari = (int)(kari/10);
}
if(atai>=a&&atai<=b){
result += i+1;
}
}
System.out.println(result);
sc.close();
}
}