import java.util.Scanner;
public class Main {
public static void main(String[] args) {
int max = Integer.MIN_VALUE;
int min = Integer.MAX_VALUE;
long sum = 0;
Scanner x = new Scanner(System.in);
int n = x.nextInt();
for (int i = 0 ; i < n ; i++){
int a = x.nextInt();
sum = sum +a;
if(max < a){
max = a;
}
if(min > a){
min = a;
}
}
System.out.println(min +" "+ max +" "+ sum);
}
}