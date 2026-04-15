import java.util.Scanner;
class Main {
public static void main(String[] args) {
Scanner scanner = new Scanner(System.in);
String input = scanner.nextLine();
String []arr_input = input.split(" ");
int aux_5 = 0;
int aux_7 = 0;
for(int i=0; i<arr_input.length; i++) {
int value = Integer.parseInt(arr_input[i]);
if(value==5)aux_5++;
if(value==7)aux_7++;
}
String r = (aux_5==2 && aux_7==1)?"YES":"NO";
System.out.println(r);
}
}