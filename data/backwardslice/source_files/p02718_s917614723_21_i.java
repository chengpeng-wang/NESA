import java.util.*;
public class Main {
public static void main(String[] args) {
Scanner sc = new Scanner(System.in);
int N = sc.nextInt();
int M = sc.nextInt();
double sum = 0;
ArrayList<Integer> list = new ArrayList<Integer>();
int count = 0;
String ans;
for (int i=0; i<N; i++) {
int num = sc.nextInt();
list.add(num);
sum += num;
}
Collections.sort(list,Collections.reverseOrder());
for (int i=0; i<N; i++) {
if (list.get(i) < sum/(4*M)) {
count = i;
break;
} else if (i == N-1) {
count = N;
break;
}
}
if (count >= M)
ans = "Yes";
else
ans = "No";
System.out.println(ans);
}
}  