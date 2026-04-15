import java.util.Scanner;
public class Main {
public static void main(String[] args) throws Exception {
Scanner sc = new Scanner(System.in);
char[] s = sc.next().toCharArray();
sc.close();
int[] a = new int[4];
for (int i = 0; i < 4; i++) {
a[i] = s[i] - '0';
}
int end = 1 << 3;
for (int i = 0; i < end; i++) {
int val = a[0];
StringBuilder sb = new StringBuilder();
sb.append(s[0]);
for (int j = 0; j < 3; j++) {
if ((i >> j & 1) == 1) {
val += a[j + 1];
sb.append('+').append(s[j + 1]);
} else {
val -= a[j + 1];
sb.append('-').append(s[j + 1]);
}
}
if (val == 7) {
sb.append("=7");
System.out.println(sb.toString());
return;
}
}
}
}