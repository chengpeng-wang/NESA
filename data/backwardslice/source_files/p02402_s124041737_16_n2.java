import java.io.*;
import java.util.*;
class Main {
public static void main(String[] args) throws Exception {
BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
br.readLine();
List<String> a=Arrays.asList(br.readLine().split(" "));
int n1=Integer.parseInt(a.get(0));
int min=n1,max=n1;
long sum=n1;
for(Iterator<String> i=a.iterator();i.hasNext();){
String s=i.next();
int n2=Integer.parseInt(s);
if(n2<min) min=n2;
if(n2>max) max=n2;
sum+=n2;
}
System.out.println(min+" "+max+" "+(sum-n1));
}
}