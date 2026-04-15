import java.util.*;
public class Main {
public static void main(String[] args ) throws Exception {
Scanner sc = new Scanner(System.in);
int count1 = 0;
int count2 = 0;
for (int i = 0; i < 3; i++) {
int token1 = sc.nextInt();
if(token1 == 7 && count1 <= 1) {
count1++;
}else if(token1 == 5 && count1 <= 2) {
count2++;
}
}
if(count1 == 1 && count2 == 2) {
System.out.println("YES");
}else {
System.out.println("NO");
}
}
}