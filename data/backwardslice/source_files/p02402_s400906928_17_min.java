import java.util.Scanner;
class Main {
public static void main(String[] args) {
Scanner scan = new Scanner(System.in);
int n,num,min,max;
long sum = 0;
n =Integer.parseInt(scan.nextLine());
String[] str = scan.nextLine().split(" ");
num = 0;
min = 1000000;
max = -1000000;
for(int i=0; i<n; i++){
num = Integer.parseInt(str[i]);
if(num>max){
max = num;
}
if(num<min){
min = num;
}
sum += num;
}
System.out.println(min + " " + max + " " + sum);
}
} 