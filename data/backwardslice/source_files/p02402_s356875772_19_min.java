import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
public class Main {
public static void main(String[] args) throws NumberFormatException, IOException{
BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
String str = br.readLine();
String[] arr = br.readLine().split(" ");
int n = Integer.parseInt(str);
int value = 0;
int max=-2147483648;
int min=2147483647;
long sum = 0 ;
for(int i=0;i<n;i++){
value = Integer.parseInt(arr[i]);
if(max<value){
max=value;
}
if(min>value){
min=value;
}
sum+=(long)value;
}
System.out.println(min +" "+ max +" "+sum);
}
}