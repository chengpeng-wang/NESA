import java.util.*;
public class Main {
public static void main(String[] args) throws Exception {
Scanner sc = new Scanner(System.in);
int n = sc.nextInt();
int[] li = new int[9];
for(int i = 0; i < n; i++){
int ind = Math.min(8, sc.nextInt()/400);
li[ind]++;
}
int min = 0;
for(int i = 0; i < 9; i++){
if(i == 8 && min != 0){
continue;
}else if(li[i] != 0){
min++;
}
}
int max = 0;
for(int i = 0; i < 9; i++){
if(i == 8){
max += li[i];
}else if(li[i] != 0){
max++;
}
}
System.out.println(min + " " + max);
}
}