package com.cmcm.baseapi;

public interface ILogger {
    void d(String str);

    void d(String str, Throwable th);

    void e(String str);

    void e(String str, Throwable th);

    void i(String str);

    void i(String str, Throwable th);

    void v(String str);

    void v(String str, Throwable th);

    void w(String str);

    void w(String str, Throwable th);
}
