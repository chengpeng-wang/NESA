import java.util.*;
public class Main {
public static void main (String[] args) {
Scanner in = new Scanner(System.in);
int ollm = in.nextInt();
int hous = in.nextInt();
int fast = in.nextInt();
int memo = fast;
int second =  0;
int last = 0;
int now =  0;
long sum = 0;
long maxm = 0;
for(int i = 0;i < hous -1;i++){
second = in.nextInt();
now = second - fast;
if(maxm < now){
maxm = now;
}
sum = sum + now;
fast = second;
}
last = ollm-(second - memo);
sum = sum + last;
if(maxm< last){
maxm = last;
}
System.out.println(sum - maxm);
}
}