import java.util.*;
class Main{
public static void main(String[] args){
Scanner sc = new Scanner(System.in);
int n = sc.nextInt();
int k = sc.nextInt();
List<Integer> hp = new ArrayList<>();
long ans = 0;
for(int i = 0;i < n;i++){
hp.add(sc.nextInt());
}
Collections.sort(hp);
for(int i = 0; i < n-k;i++){
ans += hp.get(i);
}
System.out.println(ans);
}
}