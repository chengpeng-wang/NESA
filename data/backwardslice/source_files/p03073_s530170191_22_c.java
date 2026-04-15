import java.util.*;
public class Main{
public static void main(String[] args) {
Scanner sc = new Scanner(System.in);
String s = sc.next();
int lng = s.length();
char[] c = new char[lng];
for(int i=0;i<lng;i++){
c[i] = s.charAt(i);
}
int ans1 = 0;
int ans2 = 0;
for(int i=0;i<lng;i++){
if(i%2==0 && c[i]=='1'){
ans1++;
}
if(i%2==1 && c[i]=='0'){
ans1++;
}
}
for(int i=0;i<lng;i++){
if((i+1)%2==1 && c[i]=='0'){
ans2++;
}
if((i+1)%2==0 && c[i]=='1'){
ans2++;
}
}
System.out.println(Math.min(ans1,ans2));
}
}