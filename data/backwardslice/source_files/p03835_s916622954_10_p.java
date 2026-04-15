import java.util.Scanner;
public class Main {
public static void main(String[] args) {
Scanner sc = new Scanner(System.in);
int K=sc.nextInt();
int S=sc.nextInt();
int count=0;
for(int p=0;p<=K;p++) {
for(int q=0;q<=K;q++) {
if(S-p-q<=K&&0<=S-p-q) {
count++;
}
}
}
System.out.println(count);
}
}