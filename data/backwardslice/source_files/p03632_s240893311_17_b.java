import java.util.Scanner;
public class Main {
public static void main(String[] args) {
Scanner sc = new Scanner(System.in);
int a = sc.nextInt();
int b = sc.nextInt();
int c = sc.nextInt();
int d = sc.nextInt();
int start = 0;
int end = 100;
int res = 0;
if(a <= c){
start = c;
} else {
start = a;
}
if(b <= d){
end = b;
} else {
end = d;
}
res = end - start;
if(res < 0){
res = 0;
}
System.out.println(res);
sc.close();
}
}