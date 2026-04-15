import java.util.*;
public class Main {
public static void main(String[] args) {
Scanner sc = new Scanner(System.in);
List<Integer> list = new ArrayList<>();
int n = sc.nextInt();
while(sc.hasNext()){
list.add(sc.nextInt());
}
int sum = 100000000;
int challengeSum;
for(int i = 0; i<101; i++){
challengeSum = 0;
for(int j=0; j<n; j++){
challengeSum = challengeSum + (list.get(j) - i) * (list.get(j) - i);
}
if(sum>challengeSum){
sum = challengeSum;
}
}
System.out.println(sum);
}
}