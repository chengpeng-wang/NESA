import java.util.*;
public class Main {
public static void main(String[] args) {
Scanner scanner = new Scanner(System.in);
int N = scanner.nextInt();
String output = "Yes";
String[] W = new String[N];
HashMap<String, Boolean> hmap = new HashMap<String, Boolean>();
for(int i=0; i<N; i++) {
W[i] = scanner.next();
if(hmap.get(W[i]) != null)
output = "No";
hmap.put(W[i], true);
if((i!=0) && (W[i].charAt(0) != (W[i-1].charAt(W[i-1].length()-1))))
output = "No";
}
System.out.println(output);
}
}