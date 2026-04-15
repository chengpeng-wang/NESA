import java.util.Scanner;
public class Main {
public static void main(String[] args) {
Scanner sc = new Scanner(System.in);
int aStart = sc.nextInt();
int aEnd = sc.nextInt();
int bStart = sc.nextInt();
int bEnd = sc.nextInt();
sc.close();
int start = aStart > bStart ? aStart : bStart;
int end = aEnd > bEnd ? bEnd : aEnd;
int answer = end - start;
if (aEnd < bStart || bEnd < aStart) {
System.out.println("0");
} else {
System.out.println(answer);
}
}
}