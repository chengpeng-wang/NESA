import java.util.Scanner;
public class Main {
public static void main(String[] args) {
Scanner sc = new Scanner(System.in);
int n = Integer.parseInt(sc.next());
int[] A = new int[n];
for(int i=0;i<n;i++){
A[i]= Integer.parseInt(sc.next());
}
for (int i=1; i<n;i++){
for(int k=0;k<n-1;k++){
System.out.print(A[k]+" ");
}
System.out.println(A[n-1]);
int key = A[i];
int j=i-1;
while(j>=0 && A[j]>key){
A[j+1]=A[j];
j--;
}
A[j+1]=key;
}
for(int k=0;k<n-1;k++){
System.out.print(A[k]+" ");
}
System.out.println(A[n-1]);
}
}