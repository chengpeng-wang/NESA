import java.util.Scanner;
public class Main {
public static void main(String[] args) {
Scanner sc = new Scanner(System.in);
int k = Integer.parseInt(sc.next());
int s = Integer.parseInt(sc.next());
int ans = 0;
for(int x=0; x<=k; x++){
for(int y=0; y<=k; y++){
if (s-x-y<=k && 0<=s-x-y){
ans += 1;
}
}
}
System.out.println(ans);
}
}