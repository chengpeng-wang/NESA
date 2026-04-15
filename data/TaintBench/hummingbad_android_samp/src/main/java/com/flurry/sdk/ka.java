package com.flurry.sdk;

public final class ka extends lz {
    final /* synthetic */ byte[] a;
    final /* synthetic */ String b;
    final /* synthetic */ String c;
    final /* synthetic */ jx d;

    public ka(jx jxVar, byte[] bArr, String str, String str2) {
        this.d = jxVar;
        this.a = bArr;
        this.b = str;
        this.c = str2;
    }

    public final void safeRun() {
        jx jxVar = this.d;
        byte[] bArr = this.a;
        String str = this.b;
        str = jxVar.e + str + "_" + this.c;
        kg kgVar = new kg(bArr);
        String str2 = kgVar.a;
        new ii(hz.a.b.getFileStreamPath(kg.a(str2)), ".yflurrydatasenderblock.", 1, new kc(jxVar)).a(kgVar);
        iw.a(5, jxVar.b, "Saving Block File " + str2 + " at " + hz.a.b.getFileStreamPath(kg.a(str2)));
        jxVar.d.a(kgVar, str);
    }
}
