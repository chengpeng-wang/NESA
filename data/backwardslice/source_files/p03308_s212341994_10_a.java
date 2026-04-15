import java.util.*;
import java.io.*;
import java.lang.*;
public class Main {
public static void main(String[] args) {
Scanner sc = new Scanner(System.in);
int n = sc.nextInt();
ArrayList<Integer> arr = new ArrayList<>();
for (int i = 0; i<n; i++){
int a = sc.nextInt();
arr.add(a);
}
Collections.sort(arr);
System.out.println(arr.get(n-1)-arr.get(0));
}
}