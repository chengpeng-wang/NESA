import java.util.Scanner;
class Main {
public static void main(String[] args){
Scanner scan = new Scanner(System.in);
int input = scan.nextInt();
int[] num = new int[input];
int v;
int j;
for(int i = 0; i < input; i++){
num[i] = scan.nextInt();
}
for(int i = 1; i <= input - 1; i++){
for(int n = 0; n < input; n++){
if(n != input - 1){
System.out.print(num[n] + " ");
}else{
System.out.print(num[n]);
}
}
System.out.println("");
v = num[i];
j = i - 1;
while(j >= 0 && num[j] > v){
num[j + 1] = num[j];
j--;
num[j + 1] = v;
}
}
for(int n = 0; n < input; n++){
if(n != input - 1){
System.out.print(num[n] + " ");
}else{
System.out.println(num[n]);
}
}
}
}