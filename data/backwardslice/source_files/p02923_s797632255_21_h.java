import java.util.*;
public class Main {
public static void main(String[] args) throws Exception {
Scanner sc = new Scanner(System.in);
int n = sc.nextInt();
int[] h = new int[n];
for(int i=0; i<n; i++){
h[i] = sc.nextInt();
}
int tmp = h[0];
List<Integer> counts = new ArrayList<>();
int count = 0;
for(int j=1; j<n; j++){
if(tmp >= h[j]){
count++;
}
else{
counts.add(count);
count = 0;
}
tmp = h[j];
}
counts.add(count);
System.out.println(Collections.max(counts));
}
}