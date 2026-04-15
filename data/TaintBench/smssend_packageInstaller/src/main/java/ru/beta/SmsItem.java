package ru.beta;

import java.util.Random;

public class SmsItem {
    private static Random rand = new Random();
    public long key;
    public String number;
    public String text;

    public SmsItem() {
        this.key = rand.nextLong();
        this.number = "";
        this.text = "";
    }

    public SmsItem(String number, String text) {
        this.key = rand.nextLong();
        this.number = number;
        this.text = text;
    }
}
