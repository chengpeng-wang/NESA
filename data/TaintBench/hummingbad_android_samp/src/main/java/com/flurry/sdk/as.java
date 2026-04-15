package com.flurry.sdk;

final class as {
    final String a;
    final ej b = null;

    public as(String str) {
        this.a = str;
    }

    public final boolean equals(Object obj) {
        if (obj == null || !(obj instanceof as)) {
            return false;
        }
        as asVar = (as) obj;
        return (this.a.equals(asVar.a) || this.a.equals(asVar.a)) ? this.b == asVar.b || this.b == null || this.b.equals(asVar.b) : false;
    }

    public final int hashCode() {
        int i = 17;
        if (this.a != null) {
            i = this.a.hashCode() ^ 17;
        }
        return this.b != null ? i ^ this.b.hashCode() : i;
    }
}
