import java.util.Scanner;
public class Main {
private static int[] minSegment;
private static int n;
public static void main(String[] args) {
Scanner sc = new Scanner(System.in);
int n = sc.nextInt();
int[] hList = new int[n];
for(int i = 0; i < n; i++) {
hList[i] = sc.nextInt();
}
int count = 0;
boolean allzero = false;
while(!allzero) {
allzero = true;
boolean zeroFlg = true;
for(int i = 0; i < n; i++) {
int h = hList[i];
if(h > 0) {
if(zeroFlg) {
count++;
}
hList[i] = h-1;
zeroFlg = false;
allzero = false;
} else {
zeroFlg = true;
}
}
}
System.out.println(count);
}
}