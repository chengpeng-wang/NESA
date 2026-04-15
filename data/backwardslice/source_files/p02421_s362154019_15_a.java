import java.util.Scanner;
class Main{
public static void main(String[] args){
Scanner sc = new Scanner(System.in);
int n = sc.nextInt();
int Apoint = 0;
int Bpoint = 0;
for(int i=0;i<n;i++){
String strA = sc.next();
String strB = sc.next();
int a = strA.compareTo(strB);
int b = strB.compareTo(strA);
if(a > b){
Apoint += 3;
}else if(a < b){
Bpoint += 3;
}else{
Apoint++;
Bpoint++;
}
}
System.out.println(Apoint + " " + Bpoint);
}
}