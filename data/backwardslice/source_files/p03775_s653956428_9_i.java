import java.util.Scanner;
public class Main {
public static void main(String[] args) {
Scanner sc = new Scanner(System.in);
long n = sc.nextLong();
int sqrt = (int) Math.floor(Math.sqrt(n));
int min_Keta = String.valueOf(n).length();
for (int i = 2; i <= sqrt ; i++){
if (n % i == 0){
min_Keta = Math.min(min_Keta, Math.max(String.valueOf(i).length(),String.valueOf(n / i).length()));
}
}
System.out.println(min_Keta);
}
}