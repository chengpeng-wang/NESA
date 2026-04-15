import java.util.*;
public class Main {
public static void main(String[] args) {
Scanner sc = new Scanner(System.in);
int A = sc.nextInt();
int B = sc.nextInt();
int T = sc.nextInt();
int kotae = 0;
for(int i=1; i<T+1; i++){
if(i%A==0){
kotae = kotae + B;
} else{
kotae = kotae;
}
}
System.out.println(kotae);
}
}