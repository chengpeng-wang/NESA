import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
class Main {
public static void main(String[] args) throws IOException {
BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
String s = br.readLine();
String t = br.readLine();
int ans = Integer.MAX_VALUE;
for (int i = 0; i < s.length(); i++) {
int misMatchCount = 0;
boolean  flag = true;
for (int j = 0; j < t.length(); j++) {
if(i + j >= s.length()){
flag = false;
break;
}
if(s.charAt(i+ j) != t.charAt(j)) {
++misMatchCount;
}
}
if(flag)
ans = Math.min(ans,misMatchCount);
}
System.out.println(ans);
}
}