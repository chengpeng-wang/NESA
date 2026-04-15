import java.util.*;
public class Main{
public static void main(String[] args){
Scanner scan = new Scanner(System.in);
String numM = scan.next();
char targetP = '+';
char targetM = '-';
int startNum = 0;
int counterP = 0;
int counterM = 0;
for(char Ans: numM.toCharArray()){
if(Ans==targetP){
counterP++;
}else if (Ans == targetM){
counterM++;
}
}
System.out.println((startNum+counterP-counterM));
}
}