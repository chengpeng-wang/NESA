package com.mb.bdapp.util;

import android.content.Context;
import android.text.TextUtils;

public class ClsUtils {
    public static Class<?> generateClasses(Context context, String name) {
        if (TextUtils.isEmpty(name)) {
            System.out.println("ClassNotFoundException:" + name);
            return null;
        }
        Class<?> objct = null;
        Exception exp = null;
        try {
            objct = Class.forName(name);
        } catch (Exception e) {
            exp = e;
        }
        if (exp != null) {
        }
        if (objct == null) {
            try {
                objct = Class.forName(name, false, ClsUtils.class.getClassLoader());
            } catch (Exception e2) {
            }
        }
        if (objct == null) {
            try {
                objct = Class.forName(name, false, context.getClassLoader());
            } catch (Exception e3) {
            }
        }
        if (objct == null) {
            try {
                objct = Class.forName(name, false, context.getApplicationContext().getClassLoader());
            } catch (Exception e4) {
            }
        }
        if (objct != null) {
            return objct;
        }
        System.out.println("ClassNotFoundException:" + name);
        return objct;
    }
}
