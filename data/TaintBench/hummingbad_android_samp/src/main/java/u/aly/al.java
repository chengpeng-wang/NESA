package u.aly;

import android.os.AsyncTask;

/* compiled from: ReportClient */
public class al extends ao {
    private static final String a = al.class.getName();

    /* compiled from: ReportClient */
    public interface a {
        void a();

        void a(u.aly.an.a aVar);
    }

    /* compiled from: ReportClient */
    private class b extends AsyncTask<Integer, Integer, u.aly.an.a> {
        private am b;
        private a c;

        public b(am amVar, a aVar) {
            this.b = amVar;
            this.c = aVar;
        }

        /* access modifiers changed from: protected */
        public void onPreExecute() {
            if (this.c != null) {
                this.c.a();
            }
        }

        /* access modifiers changed from: protected */
        /* renamed from: a */
        public void onPostExecute(u.aly.an.a aVar) {
            if (this.c != null) {
                this.c.a(aVar);
            }
        }

        /* access modifiers changed from: protected|varargs */
        /* renamed from: a */
        public u.aly.an.a doInBackground(Integer... numArr) {
            return al.this.a(this.b);
        }
    }

    public u.aly.an.a a(am amVar) {
        an anVar = (an) a((ap) amVar, an.class);
        return anVar == null ? u.aly.an.a.FAIL : anVar.a;
    }

    public void a(am amVar, a aVar) {
        try {
            new b(amVar, aVar).execute(new Integer[0]);
        } catch (Exception e) {
            aj.b(a, "", e);
            if (aVar != null) {
                aVar.a(u.aly.an.a.FAIL);
            }
        }
    }
}
