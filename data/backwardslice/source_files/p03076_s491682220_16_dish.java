import java.util.Scanner;
public class Main {
public static void main(String[] args) {
Scanner sc = new Scanner(System.in);
int dish;
int time = 0;
int diff = 10;
for(int i = 0; i < 5; i++) {
dish = sc.nextInt();
if(dish % 10 == 0) {
time += dish;
} else {
if(diff > dish % 10) {
diff = dish % 10;
}
time += dish + 10 - dish % 10;
}
}
if(diff != 10) {
time -= (10 - diff);
}
System.out.println(time);
}
}