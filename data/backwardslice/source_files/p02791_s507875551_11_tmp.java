import java.util.*;
class Main {
public static void main(String[] args){
Scanner sc = new Scanner(System.in);
int n = sc.nextInt();
Set<Integer> list = new TreeSet<Integer>();
int count =1;
int min = sc.nextInt();
for(int i = 1;i<n;i++){
int tmp = sc.nextInt();
if(tmp<=min){
count ++;
min = tmp;
}
}
System.out.println(count);
}
}