package com.cleanmaster.util;

import com.moceanmobile.mast.Defaults;
import com.moceanmobile.mast.MASTNativeAdConstants;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Pattern;

/* compiled from: IniFileUtils */
public class k {
    private String a = null;
    private String b = Defaults.ENCODING_UTF_8;
    private Map c = new LinkedHashMap();
    private File d = null;

    public Object a(String str, String str2) {
        return a(str, str2, null);
    }

    public Object a(String str, String str2, String str3) {
        l lVar = (l) this.c.get(str);
        if (lVar == null) {
            return null;
        }
        String a = lVar.a(str2);
        return (a == null || a.toString().trim().equals("")) ? str3 : a;
    }

    public k(File file) {
        this.d = file;
        a(file);
    }

    private void a(File file) {
        try {
            a(new BufferedReader(new FileReader(file)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void a(BufferedReader bufferedReader) {
        l lVar = null;
        Pattern compile = Pattern.compile("^\\[.*\\]$");
        while (true) {
            try {
                String readLine = bufferedReader.readLine();
                if (readLine == null) {
                    bufferedReader.close();
                    return;
                } else if (compile.matcher(readLine).matches()) {
                    readLine = readLine.trim();
                    lVar = new l(this);
                    lVar.b = readLine.substring(1, readLine.length() - 1);
                    this.c.put(lVar.b, lVar);
                } else {
                    String[] split = readLine.split(MASTNativeAdConstants.EQUAL);
                    if (split.length == 2) {
                        lVar.a(split[0], split[1]);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }
    }
}
