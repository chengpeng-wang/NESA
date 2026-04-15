package com.splunk.mint;

import com.splunk.mint.TransactionsDatabase.Container;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.Iterator;
import java.util.Map.Entry;

public class ExceptionHandler implements UncaughtExceptionHandler {
    private UncaughtExceptionHandler defaultExceptionHandler;

    public ExceptionHandler(UncaughtExceptionHandler pDefaultExceptionHandler) {
        this.defaultExceptionHandler = pDefaultExceptionHandler;
    }

    public void uncaughtException(Thread t, Throwable e) {
        Writer stacktrace = new StringWriter();
        e.printStackTrace(new PrintWriter(stacktrace));
        ActionError crashData = new ActionError(EnumActionType.error, stacktrace.toString(), EnumExceptionType.UNHANDLED, null);
        Iterator<Entry<String, Container>> iterator = Properties.transactionsDatabase.entrySet().iterator();
        while (iterator.hasNext()) {
            Entry<String, Container> pair = (Entry) iterator.next();
            if (pair.getValue() != null) {
                ActionTransactionStop.createTransactionFail(((String) pair.getKey()).replace("TStart:name:", ""), crashData.getErrorHash(), null).save(new DataSaver());
            }
            iterator.remove();
        }
        crashData.send(new NetSender(), true);
        new CrashInfo().saveLastCrashID(String.valueOf(crashData.getErrorHash()));
        new CrashInfo().saveCrashCounter();
        Utils.setForceSendPingOnNextStart();
        if (Mint.mintCallback != null) {
            Mint.mintCallback.lastBreath(new Exception(e));
        }
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
        this.defaultExceptionHandler.uncaughtException(t, e);
    }
}
