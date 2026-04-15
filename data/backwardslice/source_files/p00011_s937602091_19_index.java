import java.util.*;
class Main {
public static void main(String[] args) {
Scanner scan = new Scanner(System.in);
Main main = new Main();
int w = scan.nextInt();
int n = scan.nextInt();
ArrayList<Integer> list = new ArrayList<Integer>();
for(int i = 0; i < w; i++) {
list.add(i + 1);
}
int a, b, index = 0;
for(int i = 0; i < n; i++) {
String[] str = scan.next().split(",");
a = Integer.valueOf(str[0]);
b = Integer.valueOf(str[1]);
for(int j = 0; j < w; j++) {
if( a-1 == j) {
index = j;
continue;
}
if( b-1 == j) {
int temp = list.get(index);
list.set(index, list.get(j));
list.set(j, temp);
}
}
}
for(int i : list) {
System.out.println(i);
}
}
}