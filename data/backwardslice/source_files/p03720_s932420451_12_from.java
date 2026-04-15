import java.util.Scanner;
class Main {
public static void main(String[] args) {
Scanner sc = new Scanner(System.in);
int numberOfCity = sc.nextInt();
int numberOfRoad = sc.nextInt();
City[] cities = new City[numberOfCity];
for (int i = 0; i < numberOfCity; i++) {
cities[i] = new City();
}
for (int i = 0; i < numberOfRoad; i ++) {
int from = sc.nextInt();
int to   = sc.nextInt();
cities[from-1].addRoad();
cities[to-1].addRoad();
}
for(City city : cities) {
System.out.println(city.getCnt());
}
}
private static class City {
private int cntRoad;
public void addRoad() {
cntRoad ++;
}
public int getCnt() {
return this.cntRoad;
}
}
}