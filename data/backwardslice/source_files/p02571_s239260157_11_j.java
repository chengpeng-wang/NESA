import java.util.Scanner;
class Main {
public static void main(String[] args) {
final Scanner sc = new Scanner(System.in);
final String s = sc.nextLine();
final String t = sc.nextLine();
int min_diff = s.length();
for (int i = 0; i <= (s.length() - t.length()); i++) {
int j = 0;
int diff = 0;
while (j < t.length()) {
if (s.charAt(i + j) != t.charAt(j)) {
diff++;
}
j++;
}
if (min_diff > diff) {
min_diff = diff;
}
}
System.out.println(min_diff);
}
}