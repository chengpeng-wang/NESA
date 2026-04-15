import java.util.Scanner;
public class Main {
public static void main(String[] args) {
Scanner sc = new Scanner(System.in);
int n = sc.nextInt();
int maxValue = -1999999999;
int min = sc.nextInt();
for(int i = 1; i < n; i++) {
int nowNum = sc.nextInt();
int maxValueCandidate = nowNum - min;
if (maxValue < maxValueCandidate) {
maxValue = maxValueCandidate;
}
if(min > nowNum) {
min = nowNum;
}
}
sc.close();
System.out.println(maxValue);
}
}