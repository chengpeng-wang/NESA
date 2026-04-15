import java.util.*;
public class Main {
public static void main(String[] args) {
Scanner s = new Scanner(System.in);
int a = s.nextInt();
int b = s.nextInt();
int answer;
if (a >= 10 || b >= 10)
answer = -1;
else
answer = a * b;
System.out.println(answer);
}
}