import java.util.*;
public class Main {
public static void main(String[] args) {
Scanner stdIn = new Scanner(System.in);
long N = stdIn.nextLong();
long A = stdIn.nextLong();
long B = stdIn.nextLong();
stdIn.close();
long Count = 0;
if (N % (A + B) > A) {
Count = A * (N / (A + B)) + A;
} else {
Count = A * (N / (A + B)) + (N % (A + B));
}
System.out.println(Count);
}
}