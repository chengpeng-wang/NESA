import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Arrays;
import java.util.Collections;
import java.util.*;
class Main{
public static void main(String[] args){
Scanner scan = new Scanner(System.in);
int n = scan.nextInt();
int d[] = new int[n];
int sum = 0;
for (int i = 0;i < n;i++) {
d[i] = scan.nextInt();
}
for (int i = 0;i < n;i++) {
for (int j = i + 1;j < n;j++) {
sum += d[i] * d[j];
}
}
System.out.println(sum);
}
}