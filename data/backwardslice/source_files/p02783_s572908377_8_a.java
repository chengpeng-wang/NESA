import java.util.*;
class Main {
public static void main(String[]agrs) {
Scanner scanner = new Scanner(System.in);
int h = scanner.nextInt();
int a = scanner.nextInt();
int aN = 0;
if(h<a){
System.out.println(1);
}else{
for(int i=h; i>0; i -=a){
aN ++;
}
System.out.println(aN);
}
}
}