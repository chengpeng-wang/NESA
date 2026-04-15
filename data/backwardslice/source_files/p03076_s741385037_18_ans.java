import java.util.*;
import java.lang.*;
public class Main {
public static void main(String[] args) {
Scanner sc = new Scanner(System.in);
int[] input = new int[5];
int[] roundUp = new int[5];
int max = 0, index = 0, ans = 0;
for(int i = 0; i < 5; i++){
input[i] = sc.nextInt();
roundUp[i] = (input[i] + 9) / 10 * 10;
if(max < roundUp[i] - input[i]){
max = roundUp[i] - input[i];
index = i;
}
}
for(int i = 0; i < 5; i++){
ans += roundUp[i];
}
ans -= max;
System.out.println(ans);
}
}