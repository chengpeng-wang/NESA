import java.util.*;
import java.io.*;
public class Main {
public static void main(String[] args) throws Exception {
Scanner sc = new Scanner(System.in);
String s = sc.next();
String t = sc.next();
int count = 0;
for(int i = 0;i<s.length();i++){
char ss = s.charAt(i);
char tt = t.charAt(i);
if(ss != tt)count++;
}
System.out.println(count);
}
}