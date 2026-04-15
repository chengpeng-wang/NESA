import java.util.Scanner;
public class Main{
public static void main(String[] args){
Scanner s = new Scanner(System.in);
long max,min,sum = 0;
int n = s.nextInt();
long[] data = new long[n];
for(int i = 0;i < n;i++){
data[i] = s.nextInt();
}
max = data[0];
min = data[0];
for(int i = 0;i < n;i++){
if(data[i] >= max)
max = data[i];
if(data[i] <= min)
min = data[i];
sum += data[i];
}
System.out.println(min +" "+max+" "+sum);
}
}