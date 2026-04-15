package com.umeng.analytics;

import android.content.Context;
import u.aly.ai;
import u.aly.q;
import u.aly.w;

public class ReportPolicy {
    public static final int BATCH_AT_LAUNCH = 1;
    public static final int BATCH_BY_INTERVAL = 6;
    public static final int BATCH_BY_SIZE = 7;
    public static final int DAILY = 4;
    public static final int REALTIME = 0;
    public static final int WIFIONLY = 5;
    static final int a = 2;
    static final int b = 3;

    public static class e {
        public boolean a(boolean z) {
            return true;
        }
    }

    public static class a extends e {
        public boolean a(boolean z) {
            return z;
        }
    }

    public static class b extends e {
        private long a = 10000;
        private long b;
        private w c;

        public b(w wVar, long j) {
            this.c = wVar;
            if (j < this.a) {
                j = this.a;
            }
            this.b = j;
        }

        public boolean a(boolean z) {
            if (System.currentTimeMillis() - this.c.c >= this.b) {
                return true;
            }
            return false;
        }

        public long a() {
            return this.b;
        }
    }

    public static class c extends e {
        private final int a;
        private q b;

        public c(q qVar, int i) {
            this.a = i;
            this.b = qVar;
        }

        public boolean a(boolean z) {
            return this.b.b() > this.a;
        }
    }

    public static class d extends e {
        private long a = a.m;
        private w b;

        public d(w wVar) {
            this.b = wVar;
        }

        public boolean a(boolean z) {
            if (System.currentTimeMillis() - this.b.c >= this.a) {
                return true;
            }
            return false;
        }
    }

    public static class f extends e {
        private Context a = null;

        public f(Context context) {
            this.a = context;
        }

        public boolean a(boolean z) {
            return ai.k(this.a);
        }
    }
}
