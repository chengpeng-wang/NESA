import java.util.*;
public class Main{
public static void main(String[]args){
Scanner sc = new Scanner(System.in);
int N = Integer.parseInt(sc.nextLine());
Set<String> set = new HashSet<>();
String S = sc.nextLine();
set.add(S);
String ans = "Yes";
char c = S.charAt(S.length()-1);
for(int i = 1; i < N; i++){
String s = sc.nextLine();
int n = set.size();
set.add(s);
if(c != s.charAt(0) || n == set.size()){
ans = "No";
}
c = s.charAt(s.length()-1);
}
System.out.println(ans);
}
}