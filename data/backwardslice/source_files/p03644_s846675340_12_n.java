import java.util.*;
class Main{
public static void main(String[] args){
Scanner sc = new Scanner(System.in);
int N = Integer.parseInt(sc.next());
int ans = 1;
int max = 0;
for(int i=1; i<=N; i++){
int n = i;
int count = 0;
while(true){
if(n % 2 == 1){
break;
}else{
n /=2;
count++;
}
}
if(max < count){
ans = i;
max = count;
}
}
System.out.println(ans);
}
}