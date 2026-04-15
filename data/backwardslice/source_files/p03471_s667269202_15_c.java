import java.util.Scanner;
public class Main {
public static void main(String[] args) {
Scanner sc = new Scanner(System.in);
int n = sc.nextInt();
int y = sc.nextInt();
int man_bill = -1;
int gosen_bill = -1;
int sen_bill = -1;
final int man = 10000;
final int gosen = 5000;
final int sen = 1000;
for (int i=0; i<=n; i++) {
for (int j=0; j+i<=n; j++) {
int c= n-j-i;
int tot = i*man + j*gosen + c*sen;
if (y == tot) {
man_bill = i;
gosen_bill = j;
sen_bill = c;
}
}
}
System.out.println(man_bill + " " + gosen_bill + " " + sen_bill);
}
}