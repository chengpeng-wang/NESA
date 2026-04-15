import java.util.Arrays;
import java.util.Scanner;
public class Main {
public static void main(String[] args) {
Scanner scan = new Scanner(System.in);
int num = scan.nextInt();
int sp_move = scan.nextInt();
long[] monster = new long[num];
for (int i = 0; i < num; i++) {
monster[i] = scan.nextLong();
}
Arrays.sort(monster);
long sum = 0;
if(sp_move >= num)
System.out.println(0);
else{
for (int i = 0; i < (num-sp_move); i++) {
sum += monster[i];
}
System.out.println(sum);
}
}
}