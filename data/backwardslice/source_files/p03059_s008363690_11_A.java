import java.util.Scanner;
public class Main {
public static void main(String[] args) {
Scanner sc = new Scanner(System.in);
int A = sc.nextInt();
int B = sc.nextInt();
int T = sc.nextInt();
int cntBiscuit = 0;
int time = A;
for(int i = 2 ; time <= T ; i++) {
time = A * i;
cntBiscuit += B;
}
System.out.println(String.valueOf(cntBiscuit));
}
}