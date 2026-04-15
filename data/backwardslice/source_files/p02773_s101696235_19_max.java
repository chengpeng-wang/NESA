import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.TreeMap;
public class Main {
public static void main(String[] args) {
Scanner sc = new Scanner(System.in);
int N = sc.nextInt();
Map<String, Integer> m = new TreeMap<String, Integer>();
int max = 0;
for (int i = 0; i < N; i++) {
String S = sc.next();
if (m.containsKey(S)) {
m.put(S, m.get(S) + 1);
} else {
m.put(S, 1);
}
if (max < m.get(S)) {
max = m.get(S);
}
}
for (Entry<String, Integer> s : m.entrySet()) {
if (s.getValue() == max) {
System.out.println(s.getKey());
}
}
}
}