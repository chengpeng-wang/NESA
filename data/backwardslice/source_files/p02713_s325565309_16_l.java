import java.io.BufferedReader;
import java.io.*;
import java.util.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Collections;
public class Main {
public static void main(final String[] args){
Scanner sc = new Scanner(System.in);
int k = sc.nextInt();
int m = 0;
List<Integer> list = new ArrayList<>();
for(int i = 1; i < k + 1; i++){
for(int j = 1; j < k + 1; j++){
for(int l = 1; l < k + 1; l++){
m = gcd(i,j);
list.add(gcd(m,l));
}
}
}
int sum = 0;
for(int i = 0 ; i < k*k*k ; i++){
sum += list.get(i);
}
System.out.println(sum);
}
static int gcd (int a, int b) {
int temp;
while((temp = a%b)!=0) {
a = b;
b = temp;
}
return b;
}
}