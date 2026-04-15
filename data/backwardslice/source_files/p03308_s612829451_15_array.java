import java.util.*;
public class Main {
public static void main(String[] args) throws Exception {
Scanner sc = new Scanner(System.in);
int n = Integer.parseInt(sc.next());
int[] array = new int[n];
int max = Integer.MIN_VALUE;
int min = Integer.MAX_VALUE;
for (int i = 0; i < n; i++) {
array[i] = Integer.parseInt(sc.next());
if (min > array[i]) {
min = array[i];
}
if (max < array[i]) {
max = array[i];
}
}
System.out.println(Math.abs(max - min));
}
}