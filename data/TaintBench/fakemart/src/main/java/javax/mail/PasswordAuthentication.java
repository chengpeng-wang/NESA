package javax.mail;

public final class PasswordAuthentication {
    private String password;
    private String userName;

    public PasswordAuthentication(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public String getUserName() {
        return this.userName;
    }

    public String getPassword() {
        return this.password;
    }
}
