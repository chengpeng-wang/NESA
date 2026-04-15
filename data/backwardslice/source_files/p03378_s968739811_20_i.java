import java.util.*;
class Main {
public static void main(String[] args) {
Scanner scanner = new Scanner(System.in);
String[] nmx = scanner.nextLine().split(" ", 3);
int n = Integer.parseInt(nmx[0]);
int m = Integer.parseInt(nmx[1]);
int x = Integer.parseInt(nmx[2]);
String[] line = scanner.nextLine().split(" ", m);
int[] gates = new int[m];
for (int i = 0; i < m; i++) {
gates[i] = Integer.parseInt(line[i]);
}
int nCost = 0;
int zCost = 0;
for (int i = 0; i < m; i++) {
if (gates[i] > x) {
nCost++;
}
if (gates[i] < x) {
zCost++;
}
}
System.out.println(Math.min(nCost, zCost));
}
}