import java.util.Scanner;
public class Main {
public static void main(String[] args) {
int c0=0;
int c1=0;
int c2=0;
int c3=0;
Scanner sc = new Scanner(System.in);
int n = sc.nextInt();
String[] str = new String[n];
for(int i = 0; i<n; i++){
str[i] = sc.next();
}
sc.close();
for(int i = 0; i<n; i++){
if(str[i].equals("AC")) {
c0++;
}else if(str[i].equals("WA") ){
c1++;
}else if(str[i].equals("TLE") ){
c2++;
}else if(str[i].equals("RE")){
c3++;
}
}
System.out.println("AC x "+c0);
System.out.println("WA x "+c1);
System.out.println("TLE x "+c2);
System.out.println("RE x "+c3);
}
}