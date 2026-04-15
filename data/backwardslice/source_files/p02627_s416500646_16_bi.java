import java.util.*;
public class Main{
public static void main(String[] args) {
Scanner sc = new Scanner(System.in);
char A = sc.next().charAt(0);
char [] bi = new char[26];
char [] sm = new char[26];
char AA = 'A';
char a = 'a';
char anc = 'a';
for(int i = 0;i < 26;i++){
bi[i] = AA++;
sm[i] = a++;
}
for(int i = 0;i < 26;i++){
if(bi[i] == A){
anc = 'A';
}
}
System.out.println(anc);
}
}