import java.util.*;
public class Main {
public static void main(String[] args) {
Scanner sc = new Scanner(System.in);
long w = Long.parseLong(sc.next());
long h = Long.parseLong(sc.next());
long x = Long.parseLong(sc.next());
long y = Long.parseLong(sc.next());
double ans = (double)(w*h)/2;
int hukusu = 0;
if((double)x == (double)w/2.0 && (double)y == (double)h/2.0) {
hukusu = 1;
}
System.out.println(ans+" "+hukusu);
}
}