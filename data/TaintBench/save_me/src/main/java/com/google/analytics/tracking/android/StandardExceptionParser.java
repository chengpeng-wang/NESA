package com.google.analytics.tracking.android;

import android.content.Context;
import android.support.v4.os.EnvironmentCompat;
import java.util.Collection;
import java.util.Iterator;
import java.util.TreeSet;

public class StandardExceptionParser implements ExceptionParser {
    private final TreeSet<String> includedPackages = new TreeSet();

    public StandardExceptionParser(Context context, Collection<String> additionalPackages) {
        setIncludedPackages(context, additionalPackages);
    }

    /* JADX WARNING: Removed duplicated region for block: B:31:0x004f A:{SYNTHETIC} */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x0085  */
    public void setIncludedPackages(android.content.Context r17, java.util.Collection<java.lang.String> r18) {
        /*
        r16 = this;
        r0 = r16;
        r14 = r0.includedPackages;
        r14.clear();
        r11 = new java.util.HashSet;
        r11.<init>();
        if (r18 == 0) goto L_0x0013;
    L_0x000e:
        r0 = r18;
        r11.addAll(r0);
    L_0x0013:
        if (r17 == 0) goto L_0x004b;
    L_0x0015:
        r14 = r17.getApplicationContext();	 Catch:{ NameNotFoundException -> 0x0045 }
        r2 = r14.getPackageName();	 Catch:{ NameNotFoundException -> 0x0045 }
        r0 = r16;
        r14 = r0.includedPackages;	 Catch:{ NameNotFoundException -> 0x0045 }
        r14.add(r2);	 Catch:{ NameNotFoundException -> 0x0045 }
        r14 = r17.getApplicationContext();	 Catch:{ NameNotFoundException -> 0x0045 }
        r14 = r14.getPackageManager();	 Catch:{ NameNotFoundException -> 0x0045 }
        r15 = 15;
        r12 = r14.getPackageInfo(r2, r15);	 Catch:{ NameNotFoundException -> 0x0045 }
        r1 = r12.activities;	 Catch:{ NameNotFoundException -> 0x0045 }
        if (r1 == 0) goto L_0x004b;
    L_0x0036:
        r3 = r1;
        r7 = r3.length;	 Catch:{ NameNotFoundException -> 0x0045 }
        r5 = 0;
    L_0x0039:
        if (r5 >= r7) goto L_0x004b;
    L_0x003b:
        r13 = r3[r5];	 Catch:{ NameNotFoundException -> 0x0045 }
        r14 = r13.packageName;	 Catch:{ NameNotFoundException -> 0x0045 }
        r11.add(r14);	 Catch:{ NameNotFoundException -> 0x0045 }
        r5 = r5 + 1;
        goto L_0x0039;
    L_0x0045:
        r4 = move-exception;
        r14 = "No package found";
        com.google.analytics.tracking.android.Log.i(r14);
    L_0x004b:
        r5 = r11.iterator();
    L_0x004f:
        r14 = r5.hasNext();
        if (r14 == 0) goto L_0x008f;
    L_0x0055:
        r10 = r5.next();
        r10 = (java.lang.String) r10;
        r8 = 1;
        r0 = r16;
        r14 = r0.includedPackages;
        r6 = r14.iterator();
    L_0x0064:
        r14 = r6.hasNext();
        if (r14 == 0) goto L_0x0083;
    L_0x006a:
        r9 = r6.next();
        r9 = (java.lang.String) r9;
        r14 = r10.startsWith(r9);
        if (r14 != 0) goto L_0x008d;
    L_0x0076:
        r14 = r9.startsWith(r10);
        if (r14 == 0) goto L_0x0083;
    L_0x007c:
        r0 = r16;
        r14 = r0.includedPackages;
        r14.remove(r9);
    L_0x0083:
        if (r8 == 0) goto L_0x004f;
    L_0x0085:
        r0 = r16;
        r14 = r0.includedPackages;
        r14.add(r10);
        goto L_0x004f;
    L_0x008d:
        r8 = 0;
        goto L_0x0064;
    L_0x008f:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.analytics.tracking.android.StandardExceptionParser.setIncludedPackages(android.content.Context, java.util.Collection):void");
    }

    /* access modifiers changed from: protected */
    public Throwable getCause(Throwable t) {
        Throwable result = t;
        while (result.getCause() != null) {
            result = result.getCause();
        }
        return result;
    }

    /* access modifiers changed from: protected */
    public StackTraceElement getBestStackTraceElement(Throwable t) {
        StackTraceElement[] elements = t.getStackTrace();
        if (elements == null || elements.length == 0) {
            return null;
        }
        for (StackTraceElement e : elements) {
            String className = e.getClassName();
            Iterator i$ = this.includedPackages.iterator();
            while (i$.hasNext()) {
                if (className.startsWith((String) i$.next())) {
                    return e;
                }
            }
        }
        return elements[0];
    }

    /* access modifiers changed from: protected */
    public String getDescription(Throwable cause, StackTraceElement element, String threadName) {
        StringBuilder descriptionBuilder = new StringBuilder();
        descriptionBuilder.append(cause.getClass().getSimpleName());
        if (element != null) {
            String[] classNameParts = element.getClassName().split("\\.");
            String className = EnvironmentCompat.MEDIA_UNKNOWN;
            if (classNameParts != null && classNameParts.length > 0) {
                className = classNameParts[classNameParts.length - 1];
            }
            descriptionBuilder.append(String.format(" (@%s:%s:%s)", new Object[]{className, element.getMethodName(), Integer.valueOf(element.getLineNumber())}));
        }
        if (threadName != null) {
            descriptionBuilder.append(String.format(" {%s}", new Object[]{threadName}));
        }
        return descriptionBuilder.toString();
    }

    public String getDescription(String threadName, Throwable t) {
        return getDescription(getCause(t), getBestStackTraceElement(getCause(t)), threadName);
    }
}
