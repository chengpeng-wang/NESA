import java.util.*;
class Main{
public static void main(String[] args){
Scanner sc = new Scanner(System.in);
int n = sc.nextInt();
int x = 0;
int cout = 0;
int temp = 0;
int ans = 1;
for(int i = 1; i <= n; i++) {
x = i;
while(x % 2 == 0) {
temp++;
x /= 2;
}
if(temp > cout) {
cout = temp;
ans = i;
}
temp = 0;
}
System.out.println(ans);
}
}