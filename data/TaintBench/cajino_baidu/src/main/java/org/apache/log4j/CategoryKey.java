package org.apache.log4j;

class CategoryKey {
    static Class class$org$apache$log4j$CategoryKey;
    int hashCache;
    String name;

    CategoryKey(String name) {
        this.name = name;
        this.hashCache = name.hashCode();
    }

    public final int hashCode() {
        return this.hashCache;
    }

    public final boolean equals(Object rArg) {
        if (this == rArg) {
            return true;
        }
        if (rArg != null) {
            Class class$;
            if (class$org$apache$log4j$CategoryKey == null) {
                class$ = class$("org.apache.log4j.CategoryKey");
                class$org$apache$log4j$CategoryKey = class$;
            } else {
                class$ = class$org$apache$log4j$CategoryKey;
            }
            if (class$ == rArg.getClass()) {
                return this.name.equals(((CategoryKey) rArg).name);
            }
        }
        return false;
    }

    static Class class$(String x0) {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError(x1.getMessage());
        }
    }
}
