import java.util.Scanner;
public class Main {
public static void main(String[] args) {
Scanner scanner =new Scanner(System.in);
int n=scanner.nextInt();
int a=scanner.nextInt();
int b=scanner.nextInt();
int count=0;
for (int i = 1; i <= n; i++) {
int c=0;
for (int i1 = 0; i1 < String.valueOf(i).length(); i1++) {
c=c+ Integer.valueOf(String.valueOf(i).substring(i1, i1 + 1));
}
if (c>=a && c<=b){
count=count+i;
}
}System.out.println(count);
}
}