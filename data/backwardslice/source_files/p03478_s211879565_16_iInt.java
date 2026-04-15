import java.util.*;
public class Main {
public static void main(String[] args){
Scanner sc = new Scanner(System.in);
int N = sc.nextInt();
int A = sc.nextInt();
int B = sc.nextInt();
int ans = 0;
for (int i=1;i<=N;i++){
String iStr = String.valueOf(i);
String[] iSpl = iStr.split("");
int[] iInt = new int[iSpl.length];
int sum = 0;
for (int j=0;j<iSpl.length;j++) {
iInt[j] = Integer.parseInt(iSpl[j]);
sum = sum + iInt[j];
}
if ((sum>=A)&&(sum<=B)) {
ans = ans + i;
}
}
System.out.println(ans);
}
}