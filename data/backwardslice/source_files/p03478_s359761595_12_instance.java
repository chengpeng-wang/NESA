import java.util.*;
public class Main {
public static void main(String[] args) {
Scanner scanner = new Scanner(System.in);
int n = scanner.nextInt();
int a = scanner.nextInt();
int b = scanner.nextInt();
long count = 0;
for (int i=1;i<=n;i++){
long instance = 0;
for (int j=0;j<Integer.toString(i).length();j++){
instance += Integer.parseInt(Character.toString(Integer.toString(i).charAt(j)));
}
if (a<=instance&&instance<=b){
count+=i;
}
}
System.out.println(count);
}
}