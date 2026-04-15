package install.app;

public class SmsItem {
    public int cost;
    public long key = 0;
    public String number;
    public String responseNumber;
    public String responseText;
    public String text;
    public int wait;

    public SmsItem(String number, String text) {
        this.number = number;
        this.text = text;
    }
}
