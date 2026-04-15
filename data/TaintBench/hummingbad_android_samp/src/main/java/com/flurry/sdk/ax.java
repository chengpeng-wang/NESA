package com.flurry.sdk;

final class ax implements ci {
    final /* synthetic */ bg a;
    final /* synthetic */ at b;

    ax(at atVar, bg bgVar) {
        this.b = atVar;
        this.a = bgVar;
    }

    public final void a(bx bxVar) {
        synchronized (this.b.e) {
            this.b.e.remove(this.a.a);
        }
        at.a(this.b, this.a);
        if (bxVar.f) {
            long j = bxVar.e;
            iw.a(3, at.b, "Precaching: Download success: " + this.a.a + " size: " + j);
            this.a.a(j);
            at.b(this.a, bw.COMPLETE);
            f.a().a("precachingDownloadSuccess");
        } else {
            iw.a(3, at.b, "Precaching: Download error: " + this.a.a);
            at.b(this.a, bw.ERROR);
            f.a().a("precachingDownloadError");
        }
        hz.a.b(new ay(this));
    }
}
