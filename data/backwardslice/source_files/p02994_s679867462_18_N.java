import java.util.Scanner;
public class Main {
public static void main(String[] args) {
Scanner sc = new Scanner(System.in);
int N = Integer.parseInt(sc.next());
int L = Integer.parseInt(sc.next());
int[] Eval = new int[N];
int sum = 0;
for(int i = 0;i < N;i++) {
Eval[i] = L+ i;
sum += Eval[i];
}
sc.close();
int num = 0;
if(Eval[0] > 0) {
num = Eval[0];
}else if(Eval[N-1] < 0) {
num = Eval[N-1];
}else {
num = 0;
}
System.out.println(sum - num);
}
}