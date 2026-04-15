import java.util.*;
public class Main {
public static void main(String[] args){
Scanner sc = new Scanner(System.in);
int N = sc.nextInt();
int count = 0;
int PjMin = Integer.MAX_VALUE;
for (int i = 0; i < N; i++) {
int Pi = sc.nextInt();
if (Pi <= PjMin) {
count++;
}
if (PjMin >= Pi) {
PjMin = Pi;
}
}
System.out.println(count);
}
}