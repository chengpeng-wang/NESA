import java.util.*;
import java.io.*;
class Main{
public static void main(String[] args){
Scanner sc=new Scanner(System.in);
int n=sc.nextInt();
sc.close();
int i=0;
while(n>1){
n/=2;
i++;
}
System.out.println(1<<i);
}
}