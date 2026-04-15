import java.util.*;
public class Main {
public static void main(String[] args) {
Scanner scn = new Scanner(System.in);
Character[] let = {'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z'};
int[] enc = new int[30];
String caca = scn.next();
for(int i = 0;i<caca.length();i++) {
for(int i2 = 0;i2<let.length;i2++) {
if(let[i2]==caca.charAt(i)) {
enc[i2]++;
}
}
}boolean f = true;
for(int i = 0;i<30;i++) {
if(enc[i]%2==1)f=false;
}
if(f)System.out.println("Yes");
else System.out.println("No");
scn.close();
}
}