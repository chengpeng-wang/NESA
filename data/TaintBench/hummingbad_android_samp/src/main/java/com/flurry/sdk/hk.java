package com.flurry.sdk;

import java.io.File;
import java.io.FilenameFilter;

final class hk implements FilenameFilter {
    final /* synthetic */ hi a;

    hk(hi hiVar) {
        this.a = hiVar;
    }

    public final boolean accept(File file, String str) {
        return str.startsWith(".flurryagent.");
    }
}
