import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;
public class Main {
public static void main(String[] args) throws Exception {
Scanner sc = new Scanner(System.in);
Integer N = sc.nextInt();
Integer[] diameter = new Integer[N];
for (int i = 0; i < diameter.length; i++) {
diameter[i] = sc.nextInt();
}
Arrays.sort(diameter, Comparator.reverseOrder());
int ans = 0;
for (int i = 0; i < diameter.length; i++) {
if (i != 0 && diameter[i - 1] == diameter[i])
continue;
ans++;
}
System.out.println(ans);
sc.close();
}
}