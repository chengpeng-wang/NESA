import java.util.*;
public class Main{
public static void main(String[]args){
Scanner sc = new Scanner(System.in);
int N = sc.nextInt();
Set<Integer> set = new HashSet<Integer>();
for(int i=0;i<N;i++){
set.add(i+1);
}
int K = sc.nextInt();
int [] d = new int [K];
for(int i=0;i<K;i++){
d[i] = sc.nextInt();
for(int j=0;j<d[i];j++){
Integer[] A = new Integer[d[i]];
A[j] = sc.nextInt();
set.remove(A[j]);
}
}
System.out.println(set.size());
}
}