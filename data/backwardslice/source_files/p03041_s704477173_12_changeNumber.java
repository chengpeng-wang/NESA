import java.util.Scanner;
public class Main { 
public static void main(String[] args) {
Scanner sc = new Scanner(System.in);
String[] line = sc.nextLine().split(" ");
int strCount = Integer.parseInt(line[0]);
int changeNumber = Integer.parseInt(line[1]);
String target = sc.nextLine();
String result = "";
for(int i = 0;i<strCount;i++) {
Character tmp = target.charAt(i);
if((changeNumber - 1) == i) {
tmp = tmp.toLowerCase(tmp);
}
result += tmp;
}
System.out.println(result);
}
}