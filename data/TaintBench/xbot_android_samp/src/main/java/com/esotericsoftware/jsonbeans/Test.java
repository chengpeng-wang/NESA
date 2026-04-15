package com.esotericsoftware.jsonbeans;

import com.esotericsoftware.jsonbeans.Json.Serializer;
import java.util.ArrayList;

public class Test {

    public static class Person {
        public int age;
        public String name;
        public ArrayList numbers;

        public String getName() {
            return this.name;
        }

        public void setName(String str) {
            this.name = str;
        }

        public int getAge() {
            return this.age;
        }

        public void setAge(int i) {
            this.age = i;
        }

        public ArrayList<PhoneNumber> getNumbers() {
            return this.numbers;
        }

        public void setNumbers(ArrayList<PhoneNumber> arrayList) {
            this.numbers = arrayList;
        }
    }

    public static class PhoneNumber {
        public String name;
        public String number;

        public PhoneNumber(String str, String str2) {
            this.name = str;
            this.number = str2;
        }

        public String getName() {
            return this.name;
        }

        public void setName(String str) {
            this.name = str;
        }

        public String getNumber() {
            return this.number;
        }

        public void setNumber(String str) {
            this.number = str;
        }
    }

    public static void main(String[] strArr) throws Exception {
        Object person = new Person();
        person.setName("Nate");
        person.setAge(31);
        ArrayList arrayList = new ArrayList();
        arrayList.add(new PhoneNumber("Home", "206-555-1234"));
        arrayList.add(new PhoneNumber("Work", "425-555-4321"));
        person.setNumbers(arrayList);
        Json json = new Json();
        json.setSerializer(PhoneNumber.class, new Serializer<PhoneNumber>() {
            public void write(Json json, PhoneNumber phoneNumber, Class cls) {
                json.writeObjectStart();
                json.writeValue(phoneNumber.name, phoneNumber.number);
                json.writeObjectEnd();
            }

            public PhoneNumber read(Json json, JsonValue jsonValue, Class cls) {
                PhoneNumber phoneNumber = new PhoneNumber();
                phoneNumber.name = jsonValue.child().name();
                phoneNumber.number = jsonValue.child().asString();
                return phoneNumber;
            }
        });
        json.setElementType(Person.class, "numbers", PhoneNumber.class);
        String prettyPrint = json.prettyPrint(person);
        System.out.println(prettyPrint);
        Person person2 = (Person) json.fromJson(Person.class, prettyPrint);
    }
}
