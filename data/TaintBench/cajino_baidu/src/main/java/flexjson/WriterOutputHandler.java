package flexjson;

import java.io.IOException;
import java.io.Writer;

public class WriterOutputHandler implements OutputHandler {
    private Writer out;

    public WriterOutputHandler(Writer out) {
        this.out = out;
    }

    public OutputHandler write(String value) {
        try {
            this.out.write(value);
            return this;
        } catch (IOException e) {
            throw new JSONException("There was a problem writing output to the Writer.", e);
        }
    }

    public int write(String value, int start, int end, String append) {
        try {
            this.out.write(value, start, end - start);
            this.out.write(append);
            return end + 1;
        } catch (IOException e) {
            throw new JSONException("There was a problem writing output to the Writer.", e);
        }
    }

    public int write(String value, int start, int end) {
        try {
            this.out.write(value, start, end - start);
            return end;
        } catch (IOException e) {
            throw new JSONException("There was a problem writing output to the Writer.", e);
        }
    }

    public String toString() {
        return this.out.toString();
    }
}
