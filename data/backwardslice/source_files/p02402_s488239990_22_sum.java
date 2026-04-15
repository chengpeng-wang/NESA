import java.util.Scanner;
public class Main {
public static void main(String[] args) {
Scanner sc = new Scanner(System.in);
int count = Integer.parseInt(sc.nextLine());
int[] nums = new int[count];
String[] lines = sc.nextLine().split(" ");
for(int i = 0;i < count;i++) {
nums[i] = Integer.parseInt(lines[i]);
}
int min = nums[0];
int max = nums[0];
long sum = 0;
for(int i = 1;i < count ;i++) {
if(nums[i] < min ) {
min = nums[i];
} else if(nums[i] > max) {
max = nums[i];
}
}
for(int i = 0; i < count ; i++) {
sum += nums[i];
}
System.out.println(min + " " + max + " " + sum);
}
}