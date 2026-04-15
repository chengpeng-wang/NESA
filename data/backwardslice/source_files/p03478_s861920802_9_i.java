import java.util.Scanner;
public class Main{
public static void main(String[] args){
Scanner sc = new Scanner(System.in);
int N = sc.nextInt();
int A = sc.nextInt();
int B = sc.nextInt();
int total = 0;
for (int i = 1; i <= N ; i++) {
int count = 0;
for (String c :Integer.toString(i).split("")) {
count += Integer.parseInt(c);
}
if (A <= count && count <= B) {
total += i;
}
}
System.out.println(total);
}	
}