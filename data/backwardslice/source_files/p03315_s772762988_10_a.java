import java.util.*;
public class Main {
public static void main(String[] args){
Scanner sc = new Scanner(System.in);
String S = sc.next();
char[] a = S.toCharArray();
ArrayList<Character> aa = new ArrayList<>();
int ans =0;
for(int i=0; i<4; i++){
if(a[i] == ('+')){
ans++;
}else{
ans--;
}
}
System.out.println(ans);
}
}