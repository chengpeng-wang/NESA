import java.util.*;
import java.io.*;
public class Main {
public static void main(String[] args) throws Exception {
BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
String[] input = br.readLine().split(" ");
int a = Integer.parseInt(input[0]);
int b = Integer.parseInt(input[1]);
int c = Integer.parseInt(input[2]);
int d = Integer.parseInt(input[3]);
int ans = 0;
if(a <= c && c <= b && b <= d){
ans = b - c;
}else if(c <= a && a <= d && d <= b){
ans = d - a;
}else if(a <= c && d <= b){
ans = d - c;
}else if(c <= a && b <= d){
ans = b - a;
}else{
ans = 0;
}
out.println(ans);
out.close();
}
}