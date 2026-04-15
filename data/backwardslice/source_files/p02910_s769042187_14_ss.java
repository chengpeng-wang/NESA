import java.util.*;
public class Main {
public static void main(String[] args){
Scanner sc = new Scanner(System.in);
String S  = sc.next();
char[]ss = S.toCharArray();
int cou = 0;
for(int i=0; i<S.length(); i++){
if(i%2==0){
if(ss[i] =='L'){
cou++;
}
}else{
if(ss[i] =='R'){
cou++;
}
}
}
if(cou==0){
System.out.print("Yes");
}else{
System.out.print("No");
}
}
}