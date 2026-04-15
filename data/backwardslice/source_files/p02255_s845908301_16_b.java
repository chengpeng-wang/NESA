import java.util.Scanner;
public class Main {
public static void main(String[] args) {
Scanner sc = new Scanner(System.in);
int s = sc.nextInt();
int[] n = new int[s];
for(int i= 0; i < s; i++){
n[i] = sc.nextInt();
}
int b,c;
for(int i = 1; i < s; i++){
for(int k = 0; k < s-1; k++){
System.out.print(n[k] + " ");
}
System.out.println(n[s-1]);
b = n[i];
c = i-1;
while(c >= 0 && n[c] > b){
n[c+1] = n[c];
c--;
}
n[c+1] = b;
}
for(int k = 0; k < s-1; k++){
System.out.print(n[k] + " ") ;
}
System.out.println(n[s-1]);
}
}