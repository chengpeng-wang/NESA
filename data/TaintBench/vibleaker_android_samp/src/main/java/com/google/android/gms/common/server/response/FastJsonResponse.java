package com.google.android.gms.common.server.response;

import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.common.internal.zzx;
import com.google.android.gms.common.server.converter.ConverterWrapper;
import com.google.android.gms.internal.zzmo;
import com.google.android.gms.internal.zznb;
import com.google.android.gms.internal.zznc;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public abstract class FastJsonResponse {

    public interface zza<I, O> {
        I convertBack(O o);

        int zzrj();

        int zzrk();
    }

    public static class Field<I, O> implements SafeParcelable {
        public static final zza CREATOR = new zza();
        private final int mVersionCode;
        protected final int zzamL;
        protected final boolean zzamM;
        protected final int zzamN;
        protected final boolean zzamO;
        protected final String zzamP;
        protected final int zzamQ;
        protected final Class<? extends FastJsonResponse> zzamR;
        protected final String zzamS;
        private FieldMappingDictionary zzamT;
        /* access modifiers changed from: private */
        public zza<I, O> zzamU;

        Field(int versionCode, int typeIn, boolean typeInArray, int typeOut, boolean typeOutArray, String outputFieldName, int safeParcelableFieldId, String concreteTypeName, ConverterWrapper wrappedConverter) {
            this.mVersionCode = versionCode;
            this.zzamL = typeIn;
            this.zzamM = typeInArray;
            this.zzamN = typeOut;
            this.zzamO = typeOutArray;
            this.zzamP = outputFieldName;
            this.zzamQ = safeParcelableFieldId;
            if (concreteTypeName == null) {
                this.zzamR = null;
                this.zzamS = null;
            } else {
                this.zzamR = SafeParcelResponse.class;
                this.zzamS = concreteTypeName;
            }
            if (wrappedConverter == null) {
                this.zzamU = null;
            } else {
                this.zzamU = wrappedConverter.zzrh();
            }
        }

        protected Field(int typeIn, boolean typeInArray, int typeOut, boolean typeOutArray, String outputFieldName, int safeParcelableFieldId, Class<? extends FastJsonResponse> concreteType, zza<I, O> converter) {
            this.mVersionCode = 1;
            this.zzamL = typeIn;
            this.zzamM = typeInArray;
            this.zzamN = typeOut;
            this.zzamO = typeOutArray;
            this.zzamP = outputFieldName;
            this.zzamQ = safeParcelableFieldId;
            this.zzamR = concreteType;
            if (concreteType == null) {
                this.zzamS = null;
            } else {
                this.zzamS = concreteType.getCanonicalName();
            }
            this.zzamU = converter;
        }

        public static Field zza(String str, int i, zza<?, ?> zza, boolean z) {
            return new Field(zza.zzrj(), z, zza.zzrk(), false, str, i, null, zza);
        }

        public static <T extends FastJsonResponse> Field<T, T> zza(String str, int i, Class<T> cls) {
            return new Field(11, false, 11, false, str, i, cls, null);
        }

        public static <T extends FastJsonResponse> Field<ArrayList<T>, ArrayList<T>> zzb(String str, int i, Class<T> cls) {
            return new Field(11, true, 11, true, str, i, cls, null);
        }

        public static Field<Integer, Integer> zzi(String str, int i) {
            return new Field(0, false, 0, false, str, i, null, null);
        }

        public static Field<Double, Double> zzj(String str, int i) {
            return new Field(4, false, 4, false, str, i, null, null);
        }

        public static Field<Boolean, Boolean> zzk(String str, int i) {
            return new Field(6, false, 6, false, str, i, null, null);
        }

        public static Field<String, String> zzl(String str, int i) {
            return new Field(7, false, 7, false, str, i, null, null);
        }

        public static Field<ArrayList<String>, ArrayList<String>> zzm(String str, int i) {
            return new Field(7, true, 7, true, str, i, null, null);
        }

        public I convertBack(O output) {
            return this.zzamU.convertBack(output);
        }

        public int describeContents() {
            zza zza = CREATOR;
            return 0;
        }

        public int getVersionCode() {
            return this.mVersionCode;
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Field\n");
            stringBuilder.append("            versionCode=").append(this.mVersionCode).append(10);
            stringBuilder.append("                 typeIn=").append(this.zzamL).append(10);
            stringBuilder.append("            typeInArray=").append(this.zzamM).append(10);
            stringBuilder.append("                typeOut=").append(this.zzamN).append(10);
            stringBuilder.append("           typeOutArray=").append(this.zzamO).append(10);
            stringBuilder.append("        outputFieldName=").append(this.zzamP).append(10);
            stringBuilder.append("      safeParcelFieldId=").append(this.zzamQ).append(10);
            stringBuilder.append("       concreteTypeName=").append(zzru()).append(10);
            if (zzrt() != null) {
                stringBuilder.append("     concreteType.class=").append(zzrt().getCanonicalName()).append(10);
            }
            stringBuilder.append("          converterName=").append(this.zzamU == null ? "null" : this.zzamU.getClass().getCanonicalName()).append(10);
            return stringBuilder.toString();
        }

        public void writeToParcel(Parcel out, int flags) {
            zza zza = CREATOR;
            zza.zza(this, out, flags);
        }

        public void zza(FieldMappingDictionary fieldMappingDictionary) {
            this.zzamT = fieldMappingDictionary;
        }

        public int zzrj() {
            return this.zzamL;
        }

        public int zzrk() {
            return this.zzamN;
        }

        public Field<I, O> zzro() {
            return new Field(this.mVersionCode, this.zzamL, this.zzamM, this.zzamN, this.zzamO, this.zzamP, this.zzamQ, this.zzamS, zzrw());
        }

        public boolean zzrp() {
            return this.zzamM;
        }

        public boolean zzrq() {
            return this.zzamO;
        }

        public String zzrr() {
            return this.zzamP;
        }

        public int zzrs() {
            return this.zzamQ;
        }

        public Class<? extends FastJsonResponse> zzrt() {
            return this.zzamR;
        }

        /* access modifiers changed from: 0000 */
        public String zzru() {
            return this.zzamS == null ? null : this.zzamS;
        }

        public boolean zzrv() {
            return this.zzamU != null;
        }

        /* access modifiers changed from: 0000 */
        public ConverterWrapper zzrw() {
            return this.zzamU == null ? null : ConverterWrapper.zza(this.zzamU);
        }

        public Map<String, Field<?, ?>> zzrx() {
            zzx.zzz(this.zzamS);
            zzx.zzz(this.zzamT);
            return this.zzamT.zzcR(this.zzamS);
        }
    }

    private void zza(StringBuilder stringBuilder, Field field, Object obj) {
        if (field.zzrj() == 11) {
            stringBuilder.append(((FastJsonResponse) field.zzrt().cast(obj)).toString());
        } else if (field.zzrj() == 7) {
            stringBuilder.append("\"");
            stringBuilder.append(zznb.zzcU((String) obj));
            stringBuilder.append("\"");
        } else {
            stringBuilder.append(obj);
        }
    }

    private void zza(StringBuilder stringBuilder, Field field, ArrayList<Object> arrayList) {
        stringBuilder.append("[");
        int size = arrayList.size();
        for (int i = 0; i < size; i++) {
            if (i > 0) {
                stringBuilder.append(",");
            }
            Object obj = arrayList.get(i);
            if (obj != null) {
                zza(stringBuilder, field, obj);
            }
        }
        stringBuilder.append("]");
    }

    public String toString() {
        Map zzrl = zzrl();
        StringBuilder stringBuilder = new StringBuilder(100);
        for (String str : zzrl.keySet()) {
            Field field = (Field) zzrl.get(str);
            if (zza(field)) {
                Object zza = zza(field, zzb(field));
                if (stringBuilder.length() == 0) {
                    stringBuilder.append("{");
                } else {
                    stringBuilder.append(",");
                }
                stringBuilder.append("\"").append(str).append("\":");
                if (zza != null) {
                    switch (field.zzrk()) {
                        case 8:
                            stringBuilder.append("\"").append(zzmo.zzj((byte[]) zza)).append("\"");
                            break;
                        case 9:
                            stringBuilder.append("\"").append(zzmo.zzk((byte[]) zza)).append("\"");
                            break;
                        case 10:
                            zznc.zza(stringBuilder, (HashMap) zza);
                            break;
                        default:
                            if (!field.zzrp()) {
                                zza(stringBuilder, field, zza);
                                break;
                            }
                            zza(stringBuilder, field, (ArrayList) zza);
                            break;
                    }
                }
                stringBuilder.append("null");
            }
        }
        if (stringBuilder.length() > 0) {
            stringBuilder.append("}");
        } else {
            stringBuilder.append("{}");
        }
        return stringBuilder.toString();
    }

    /* access modifiers changed from: protected */
    public <O, I> I zza(Field<I, O> field, Object obj) {
        return field.zzamU != null ? field.convertBack(obj) : obj;
    }

    /* access modifiers changed from: protected */
    public boolean zza(Field field) {
        return field.zzrk() == 11 ? field.zzrq() ? zzcQ(field.zzrr()) : zzcP(field.zzrr()) : zzcO(field.zzrr());
    }

    /* access modifiers changed from: protected */
    public Object zzb(Field field) {
        String zzrr = field.zzrr();
        if (field.zzrt() == null) {
            return zzcN(field.zzrr());
        }
        zzx.zza(zzcN(field.zzrr()) == null, "Concrete field shouldn't be value object: %s", field.zzrr());
        Map zzrn = field.zzrq() ? zzrn() : zzrm();
        if (zzrn != null) {
            return zzrn.get(zzrr);
        }
        try {
            return getClass().getMethod("get" + Character.toUpperCase(zzrr.charAt(0)) + zzrr.substring(1), new Class[0]).invoke(this, new Object[0]);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public abstract Object zzcN(String str);

    public abstract boolean zzcO(String str);

    /* access modifiers changed from: protected */
    public boolean zzcP(String str) {
        throw new UnsupportedOperationException("Concrete types not supported");
    }

    /* access modifiers changed from: protected */
    public boolean zzcQ(String str) {
        throw new UnsupportedOperationException("Concrete type arrays not supported");
    }

    public abstract Map<String, Field<?, ?>> zzrl();

    public HashMap<String, Object> zzrm() {
        return null;
    }

    public HashMap<String, Object> zzrn() {
        return null;
    }
}
