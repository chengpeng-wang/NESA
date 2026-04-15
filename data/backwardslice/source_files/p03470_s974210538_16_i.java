import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.HashSet;
import java.util.Set;
public class Main{
public static void main(String[] args){
InputStream inputStream = System.in;
OutputStream outputStream = System.out;
Scanner sc = new Scanner(inputStream);
PrintWriter out = new PrintWriter(outputStream);
int N = Integer.parseInt(sc.next());
Set<String> list = new HashSet<String>();
for(int i = 0; i < N; i++){
list.add(sc.next());
}
System.out.println(list.size());
out.close();
}
}