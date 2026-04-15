import java.util.*;
public class Main {
public static void main(String[] args) {
Scanner sc = new Scanner(System.in);
int a = sc.nextInt();
int b = sc.nextInt();
StringBuilder A = new StringBuilder();
StringBuilder B = new StringBuilder();
for(int i=0; i<b; i++) {
A.append(a);
}
for(int i=0; i<a; i++) {
B.append(b);
}
if(a<b) {
System.out.println(A);
}else {
System.out.println(B);
}
}
}