package org.mozilla.classfile;

/* compiled from: ClassFileWriter */
final class FieldOrMethodRef {
    private String className;
    private int hashCode = -1;
    private String name;
    private String type;

    FieldOrMethodRef(String className, String name, String type) {
        this.className = className;
        this.name = name;
        this.type = type;
    }

    public String getClassName() {
        return this.className;
    }

    public String getName() {
        return this.name;
    }

    public String getType() {
        return this.type;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof FieldOrMethodRef)) {
            return false;
        }
        FieldOrMethodRef x = (FieldOrMethodRef) obj;
        if (this.className.equals(x.className) && this.name.equals(x.name) && this.type.equals(x.type)) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        if (this.hashCode == -1) {
            int h1 = this.className.hashCode();
            int h2 = this.name.hashCode();
            this.hashCode = (h1 ^ h2) ^ this.type.hashCode();
        }
        return this.hashCode;
    }
}
