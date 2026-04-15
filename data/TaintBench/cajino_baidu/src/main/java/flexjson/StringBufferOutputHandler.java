package flexjson;

public class StringBufferOutputHandler implements OutputHandler {
    private StringBuffer out;

    public StringBufferOutputHandler(StringBuffer out) {
        this.out = out;
    }

    public OutputHandler write(String value) {
        this.out.append(value);
        return this;
    }

    public int write(String value, int start, int end, String append) {
        this.out.append(value, start, end);
        this.out.append(append);
        return end + 1;
    }

    public int write(String value, int start, int end) {
        this.out.append(value, start, end);
        return end;
    }

    public String toString() {
        return this.out.toString();
    }
}
