import java.util.Scanner;
class Main{
public static void main(String[] args){
Scanner sc = new Scanner(System.in);
int[] number = new int[3];
int sum = 0;
for(int i=0;i<3;i++){
number[i] = sc.nextInt();
sum += number[i];
}
int K = sc.nextInt();
int max = number[0];
for(int j=0;j<2;j++){
if(number[j+1]>number[j]){
max = number[j+1];
}
}
int answer = (int)(sum-max+max*Math.pow(2,K));
System.out.println(answer);
}
}