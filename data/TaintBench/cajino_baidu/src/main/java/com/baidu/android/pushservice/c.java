package com.baidu.android.pushservice;

import android.app.Notification;
import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.util.Log;
import com.baidu.android.common.security.Base64;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;

public class c {
    private static String a = "NotificationBuilderManager";
    private static String b = "notification_builder_storage";
    private static Object c = new Object();
    private static int d = 0;

    public static Notification a(Context context, int i, int i2, String str, String str2) {
        Notification construct;
        synchronized (c) {
            PushNotificationBuilder a = a(context, i);
            a.setNotificationTitle(str);
            a.setNotificationText(str2);
            construct = a.construct(context);
            if ((i2 & 1) != 0) {
                construct.flags &= -33;
            } else {
                construct.flags |= 32;
            }
            if ((i2 & 4) != 0) {
                construct.defaults |= 1;
            } else {
                construct.defaults &= -2;
            }
            if ((i2 & 2) != 0) {
                construct.defaults |= 2;
            } else {
                construct.defaults &= -3;
            }
        }
        return construct;
    }

    public static Notification a(Context context, int i, String str, String str2) {
        Notification construct;
        synchronized (c) {
            PushNotificationBuilder a = a(context, i);
            a.setNotificationTitle(str);
            a.setNotificationText(str2);
            construct = a.construct(context);
        }
        return construct;
    }

    private static PushNotificationBuilder a(Context context) {
        BasicPushNotificationBuilder basicPushNotificationBuilder = new BasicPushNotificationBuilder();
        basicPushNotificationBuilder.setNotificationFlags(16);
        basicPushNotificationBuilder.setNotificationDefaults(3);
        basicPushNotificationBuilder.setStatusbarIcon(context.getApplicationInfo().icon);
        return basicPushNotificationBuilder;
    }

    private static PushNotificationBuilder a(Context context, int i) {
        StreamCorruptedException e;
        IOException e2;
        ClassNotFoundException e3;
        if (b.a()) {
            Log.e(a, "getBuilder id=" + i);
        }
        String string = context.getSharedPreferences(b, 0).getString("" + i, null);
        if (string == null) {
            return b(context);
        }
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(Base64.decode(string.getBytes()));
        PushNotificationBuilder pushNotificationBuilder;
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
            pushNotificationBuilder = (PushNotificationBuilder) objectInputStream.readObject();
            try {
                objectInputStream.close();
                byteArrayInputStream.close();
                return pushNotificationBuilder;
            } catch (StreamCorruptedException e4) {
                e = e4;
            } catch (IOException e5) {
                e2 = e5;
                Log.e(a, "getBuilder read object error");
                e2.printStackTrace();
                return pushNotificationBuilder;
            } catch (ClassNotFoundException e6) {
                e3 = e6;
                Log.e(a, "getBuilder read object error: class not found");
                e3.printStackTrace();
                return pushNotificationBuilder;
            }
        } catch (StreamCorruptedException e7) {
            StreamCorruptedException streamCorruptedException = e7;
            pushNotificationBuilder = null;
            e = streamCorruptedException;
            Log.e(a, "getBuilder read object error");
            e.printStackTrace();
            return pushNotificationBuilder;
        } catch (IOException e8) {
            IOException iOException = e8;
            pushNotificationBuilder = null;
            e2 = iOException;
            Log.e(a, "getBuilder read object error");
            e2.printStackTrace();
            return pushNotificationBuilder;
        } catch (ClassNotFoundException e9) {
            ClassNotFoundException classNotFoundException = e9;
            pushNotificationBuilder = null;
            e3 = classNotFoundException;
            Log.e(a, "getBuilder read object error: class not found");
            e3.printStackTrace();
            return pushNotificationBuilder;
        }
    }

    public static void a(Context context, int i, PushNotificationBuilder pushNotificationBuilder) {
        synchronized (c) {
            try {
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
                objectOutputStream.writeObject(pushNotificationBuilder);
                String encode = Base64.encode(byteArrayOutputStream.toByteArray(), "US-ASCII");
                Editor edit = context.getSharedPreferences(b, 0).edit();
                edit.putString("" + i, encode);
                edit.commit();
                byteArrayOutputStream.close();
                objectOutputStream.close();
            } catch (StreamCorruptedException e) {
                Log.e(a, "setNotificationBuilder write object error");
                e.printStackTrace();
            } catch (IOException e2) {
                Log.e(a, "setNotificationBuilder write object error");
                e2.printStackTrace();
            }
        }
        return;
    }

    public static void a(Context context, PushNotificationBuilder pushNotificationBuilder) {
        synchronized (c) {
            try {
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
                objectOutputStream.writeObject(pushNotificationBuilder);
                String encode = Base64.encode(byteArrayOutputStream.toByteArray(), "US-ASCII");
                Editor edit = context.getSharedPreferences(b, 0).edit();
                edit.putString("" + d, encode);
                edit.commit();
                byteArrayOutputStream.close();
                objectOutputStream.close();
            } catch (StreamCorruptedException e) {
                Log.e(a, "setDefaultNotificationBuilder write object error");
                e.printStackTrace();
            } catch (IOException e2) {
                Log.e(a, "setDefaultNotificationBuilder write object error");
                e2.printStackTrace();
            }
        }
        return;
    }

    private static PushNotificationBuilder b(Context context) {
        StreamCorruptedException e;
        IOException e2;
        ClassNotFoundException e3;
        String string = context.getSharedPreferences(b, 0).getString("" + d, null);
        if (string == null) {
            return a(context);
        }
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(Base64.decode(string.getBytes()));
        PushNotificationBuilder pushNotificationBuilder;
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
            pushNotificationBuilder = (PushNotificationBuilder) objectInputStream.readObject();
            try {
                objectInputStream.close();
                byteArrayInputStream.close();
                return pushNotificationBuilder;
            } catch (StreamCorruptedException e4) {
                e = e4;
            } catch (IOException e5) {
                e2 = e5;
                Log.e(a, "getDefaultBuilder read object error");
                e2.printStackTrace();
                return pushNotificationBuilder;
            } catch (ClassNotFoundException e6) {
                e3 = e6;
                Log.e(a, "getDefaultBuilder read object error: class not found");
                e3.printStackTrace();
                return pushNotificationBuilder;
            }
        } catch (StreamCorruptedException e7) {
            StreamCorruptedException streamCorruptedException = e7;
            pushNotificationBuilder = null;
            e = streamCorruptedException;
            Log.e(a, "getDefaultBuilder read object error");
            e.printStackTrace();
            return pushNotificationBuilder;
        } catch (IOException e8) {
            IOException iOException = e8;
            pushNotificationBuilder = null;
            e2 = iOException;
            Log.e(a, "getDefaultBuilder read object error");
            e2.printStackTrace();
            return pushNotificationBuilder;
        } catch (ClassNotFoundException e9) {
            ClassNotFoundException classNotFoundException = e9;
            pushNotificationBuilder = null;
            e3 = classNotFoundException;
            Log.e(a, "getDefaultBuilder read object error: class not found");
            e3.printStackTrace();
            return pushNotificationBuilder;
        }
    }

    public static void b(Context context, PushNotificationBuilder pushNotificationBuilder) {
        a(context, 8888, pushNotificationBuilder);
    }
}
