import java.util.*;
public class Main {
public static void main(String[] args) {
Scanner sc = new Scanner(System.in);
int a = sc.nextInt();
int b = sc.nextInt();
int h = sc.nextInt();
int m = sc.nextInt();
sc.close();
double l = m*6;
double s = h*30+m*0.5;
double cos;
if (Math.abs(l-s) > 180) {
cos = 360 - Math.abs(l-s);
} else {
cos = Math.abs(l-s);
}
double c2 = Math.pow(a, 2) + Math.pow(b, 2) - 2*a*b*Math.cos(Math.toRadians(cos));
double c = Math.sqrt(c2);
System.out.println(c);
}
}