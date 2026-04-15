import java.io.*;
public class Main{
public static void main(String[]args) throws NumberFormatException, IOException {
BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
int n = Integer.parseInt(br.readLine());
String[] str = br.readLine().split(" ");
String [] num =new String[n];
int count=n-1;
String res="";
for(int i=0;i<n;i++){
if(count==0){
num[i]=(str[count]);
res+=num[i];
}else{
num[i]=(str[count]);
count--;
res+=num[i]+" ";
}
}
System.out.println(res);
}
}