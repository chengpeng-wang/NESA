import java.util.*;
public class Main {
public static void main(String[] args ) throws Exception {
Scanner sc = new Scanner(System.in);
int A = sc.nextInt();
int B = sc.nextInt();
int C = sc.nextInt();
int max_num = Math.max(A,B);
ArrayList<Integer> num = new ArrayList<Integer>();
for(int i=1;i<=max_num;i++){
if(B%i==0&&A%i==0){
num.add(i);
}
}
int size=num.size();
System.out.println(num.get(size-C));
}
}