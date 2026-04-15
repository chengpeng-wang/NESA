import java.util.Scanner;
class Main{
public static int selectionSort(int A[],int N){
int i,j,temp,r=0,min;
for(i=0;i<N-1;i++){
min=i;
for(j=i;j<N;j++){
if(A[j]<A[min])
min=j;
}
temp=A[i];
A[i]=A[min];
A[min]=temp;
if(i!=min)
r++;
}
return r;
}
public static void main(String[] args){
Scanner sc= new Scanner(System.in);
int N=sc.nextInt();
int A[]=new int[100];
int i,j;
for(i=0;i<N;i++){
A[i] = sc.nextInt();
}
int loop = selectionSort(A,N);
for(i=0;i<N;i++){
if(i>0)
System.out.print(" ");
System.out.print(A[i]);
}
System.out.println();
System.out.println(loop);
sc.close();
}
}