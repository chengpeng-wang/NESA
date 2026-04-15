import java.util.*;
public class Main{
public static void main(String[] args){
Scanner sc = new Scanner(System.in);
int num = Integer.parseInt(sc.next());
int[] kagamiMochi = new int[num];
int count = 1;
for (int i = 0; i<num; i++){
kagamiMochi[i] = Integer.parseInt(sc.next());
}
Arrays.sort(kagamiMochi);
for(int j = num-2; j>=0; j--){
if (kagamiMochi[j] != kagamiMochi[j+1]){
count++;
}
}
System.out.println(count);
}
}