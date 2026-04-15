// ITP1_3
import java.util.Scanner;
class Main
{
public static void main ( String[] args ) {
Scanner sc = new Scanner ( System.in );
int a = 0;
int b = 0;
int c = 0;
int d = 0;
int count = 0;
a = sc.nextInt ();
b = sc.nextInt ();
c = sc.nextInt ();
d = a;
for (; a <= b; a++ ) {
if ( c % a == 0 ) {
count++;
}
}
System.out.println ( count );
}
}