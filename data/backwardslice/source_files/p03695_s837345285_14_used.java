import java.util.*;
public class Main {
public static void main(String[] args) {
Scanner sc = new Scanner(System.in);
boolean[] used=new boolean[8];
int free=0;
int n=sc.nextInt();
int tmp;
for(int i=0;i<n;i++){
tmp=sc.nextInt();
if(tmp>=3200){
free++;
}else{
used[tmp/400]=true;
}
}
int useds=0;
for(boolean a:used){
useds+=a?1:0;
}
System.out.print(Math.max(useds, 1)+" ");
System.out.println(useds+free);
}
}