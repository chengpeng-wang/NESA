import java.util.*;
public class Main {
public static void main(String[] args) {
Scanner sc = new Scanner(System.in);
String num = sc.nextLine();
String[] numArray = num.split("");
int count = 0;
for(int i = 0;i<numArray.length;i++) {
int[] numInt = new int[numArray.length];
numInt[i] = Integer.parseInt(numArray[i]);
if(numInt[i] == 1) {
count++;
}
}
System.out.println(count);
}
}