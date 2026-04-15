import java.util.*;
public class Main {
static int ans;
public static void main(String[] args) {
Scanner sc = new Scanner(System.in);
int n = sc.nextInt();
List<Integer> list = new ArrayList<Integer>();
for(int i = 0;i < n;i++) {
boolean has = false;
int d = sc.nextInt();
for(int k:list) {
if(d == k) {
has = true;
break;
}
}
if(!has) {
ans++;
list.add(d);
}
}
System.out.println(ans);
}
}