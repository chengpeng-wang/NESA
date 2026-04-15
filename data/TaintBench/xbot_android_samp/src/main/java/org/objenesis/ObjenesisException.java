package org.objenesis;

public class ObjenesisException extends RuntimeException {
    private static final boolean jdk14 = (Double.parseDouble(System.getProperty("java.specification.version")) > 1.3d);
    private static final long serialVersionUID = -2677230016262426968L;

    public ObjenesisException(String msg) {
        super(msg);
    }

    public ObjenesisException(Throwable cause) {
        super(cause == null ? null : cause.toString());
        if (jdk14) {
            initCause(cause);
        }
    }

    public ObjenesisException(String msg, Throwable cause) {
        super(msg);
        if (jdk14) {
            initCause(cause);
        }
    }
}
