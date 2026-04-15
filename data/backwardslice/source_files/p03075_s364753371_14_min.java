import java.util.*;
public class Main {
public static void main(String[] args) throws Exception {
Scanner scan = new Scanner(System.in);
int num = 0;
int min = 0;
int max = 0;
min = scan.nextInt();
scan.nextInt();
scan.nextInt();
scan.nextInt();
max = scan.nextInt();
int k = scan.nextInt();
if ((max - min) > k){
System.out.println(":(");
} else {
System.out.println("Yay!");
}
}
}