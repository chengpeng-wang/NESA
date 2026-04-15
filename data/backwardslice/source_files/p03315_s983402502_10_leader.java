import java.util.*;
public class Main{
public static void main(String[] args) {
Scanner sc = new Scanner(System.in);
String  s = sc.next();
char[] leader = s.toCharArray();
int count1 = 0;
int count2 = 0;
for(int i = 0; i < 4; i++){
if(leader[i] == '+'){
count1++;
}else{
count2++;
}
}
System.out.println(count1 - count2);
}
}