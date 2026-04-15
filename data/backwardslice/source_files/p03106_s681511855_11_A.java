import java.util.*;
public class Main {
public static void main(String[] args) throws Exception {
Scanner scanner = new Scanner(System.in);
int A = scanner.nextInt();
int B = scanner.nextInt();
int K = scanner.nextInt();
int count = 0;
List<Integer> list = new ArrayList<Integer>();
for(int i=1; i<=100; i++){
if(A%i==0 && B%i==0){
list.add(i);
count++;
}
}
Collections.sort(list, Collections.reverseOrder());
System.out.println(list.get(K-1));
}
}