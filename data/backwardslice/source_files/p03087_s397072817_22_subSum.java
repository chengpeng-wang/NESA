import java.util.Scanner;
public class Main {
public static void main(String[] args) throws Exception {
Scanner sc = new Scanner(System.in);
int N = sc.nextInt();
int Q = sc.nextInt();
String S = sc.next();
String[] SSplit = S.split("");
int[][] LR = new int[Q][2];
for (int i = 0; i < Q; i++) {
LR[i][0] = sc.nextInt();
LR[i][1] = sc.nextInt();
}
int[] subSum = new int[N];
subSum[0] = 0;
boolean mode = SSplit[0].equals("A");
for (int i = 1; i < N; i++) {
if (SSplit[i].equals("A")) {
subSum[i] = subSum[i - 1];
mode = true;
} else if (mode && SSplit[i].equals("C")) {
subSum[i] = subSum[i - 1] + 1;
mode = false;
} else {
subSum[i] = subSum[i - 1];
mode = false;
}
}
for (int i = 0; i < Q; i++) {
System.out.println(subSum[LR[i][1] - 1] - subSum[LR[i][0] - 1]);
}
}
}