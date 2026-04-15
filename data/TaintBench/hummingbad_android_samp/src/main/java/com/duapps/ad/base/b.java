package com.duapps.ad.base;

/* compiled from: ToolboxRequestHelper */
public abstract class b {
    public abstract void a(int i, T t);

    public abstract void a(int i, String str);

    public void a(Exception exception) {
        StackTraceElement[] stackTrace = exception.getStackTrace();
        StringBuilder stringBuilder = new StringBuilder("");
        stringBuilder.append(exception.getMessage());
        stringBuilder.append(10);
        for (StackTraceElement stackTraceElement : stackTrace) {
            stringBuilder.append(stackTraceElement.toString());
            stringBuilder.append(10);
            stringBuilder.append(stackTraceElement.getClassName());
            stringBuilder.append(stackTraceElement.getLineNumber());
        }
        a(-1, stringBuilder.toString());
    }
}
