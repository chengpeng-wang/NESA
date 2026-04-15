package flexjson;

public interface OutputHandler {
    int write(String str, int i, int i2);

    int write(String str, int i, int i2, String str2);

    OutputHandler write(String str);
}
