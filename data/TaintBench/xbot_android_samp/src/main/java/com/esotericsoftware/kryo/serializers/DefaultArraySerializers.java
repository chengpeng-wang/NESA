package com.esotericsoftware.kryo.serializers;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import java.lang.reflect.Array;
import java.lang.reflect.Modifier;

public class DefaultArraySerializers {

    public static class BooleanArraySerializer extends Serializer<boolean[]> {
        public BooleanArraySerializer() {
            setAcceptsNull(true);
        }

        public void write(Kryo kryo, Output output, boolean[] zArr) {
            int i = 0;
            if (zArr == null) {
                output.writeByte((byte) 0);
                return;
            }
            output.writeInt(zArr.length + 1, true);
            int length = zArr.length;
            while (i < length) {
                output.writeBoolean(zArr[i]);
                i++;
            }
        }

        public boolean[] read(Kryo kryo, Input input, Class<boolean[]> cls) {
            int readInt = input.readInt(true);
            if (readInt == 0) {
                return null;
            }
            int i = readInt - 1;
            boolean[] zArr = new boolean[i];
            for (int i2 = 0; i2 < i; i2++) {
                zArr[i2] = input.readBoolean();
            }
            return zArr;
        }

        public boolean[] copy(Kryo kryo, boolean[] zArr) {
            boolean[] zArr2 = new boolean[zArr.length];
            System.arraycopy(zArr, 0, zArr2, 0, zArr2.length);
            return zArr2;
        }
    }

    public static class ByteArraySerializer extends Serializer<byte[]> {
        public ByteArraySerializer() {
            setAcceptsNull(true);
        }

        public void write(Kryo kryo, Output output, byte[] bArr) {
            if (bArr == null) {
                output.writeByte((byte) 0);
                return;
            }
            output.writeInt(bArr.length + 1, true);
            output.writeBytes(bArr);
        }

        public byte[] read(Kryo kryo, Input input, Class<byte[]> cls) {
            int readInt = input.readInt(true);
            if (readInt == 0) {
                return null;
            }
            return input.readBytes(readInt - 1);
        }

        public byte[] copy(Kryo kryo, byte[] bArr) {
            byte[] bArr2 = new byte[bArr.length];
            System.arraycopy(bArr, 0, bArr2, 0, bArr2.length);
            return bArr2;
        }
    }

    public static class CharArraySerializer extends Serializer<char[]> {
        public CharArraySerializer() {
            setAcceptsNull(true);
        }

        public void write(Kryo kryo, Output output, char[] cArr) {
            int i = 0;
            if (cArr == null) {
                output.writeByte((byte) 0);
                return;
            }
            output.writeInt(cArr.length + 1, true);
            int length = cArr.length;
            while (i < length) {
                output.writeChar(cArr[i]);
                i++;
            }
        }

        public char[] read(Kryo kryo, Input input, Class<char[]> cls) {
            int readInt = input.readInt(true);
            if (readInt == 0) {
                return null;
            }
            int i = readInt - 1;
            char[] cArr = new char[i];
            for (int i2 = 0; i2 < i; i2++) {
                cArr[i2] = input.readChar();
            }
            return cArr;
        }

        public char[] copy(Kryo kryo, char[] cArr) {
            char[] cArr2 = new char[cArr.length];
            System.arraycopy(cArr, 0, cArr2, 0, cArr2.length);
            return cArr2;
        }
    }

    public static class DoubleArraySerializer extends Serializer<double[]> {
        public DoubleArraySerializer() {
            setAcceptsNull(true);
        }

        public void write(Kryo kryo, Output output, double[] dArr) {
            int i = 0;
            if (dArr == null) {
                output.writeByte((byte) 0);
                return;
            }
            output.writeInt(dArr.length + 1, true);
            int length = dArr.length;
            while (i < length) {
                output.writeDouble(dArr[i]);
                i++;
            }
        }

        public double[] read(Kryo kryo, Input input, Class<double[]> cls) {
            int readInt = input.readInt(true);
            if (readInt == 0) {
                return null;
            }
            int i = readInt - 1;
            double[] dArr = new double[i];
            for (int i2 = 0; i2 < i; i2++) {
                dArr[i2] = input.readDouble();
            }
            return dArr;
        }

        public double[] copy(Kryo kryo, double[] dArr) {
            double[] dArr2 = new double[dArr.length];
            System.arraycopy(dArr, 0, dArr2, 0, dArr2.length);
            return dArr2;
        }
    }

    public static class FloatArraySerializer extends Serializer<float[]> {
        public FloatArraySerializer() {
            setAcceptsNull(true);
        }

        public void write(Kryo kryo, Output output, float[] fArr) {
            int i = 0;
            if (fArr == null) {
                output.writeByte((byte) 0);
                return;
            }
            output.writeInt(fArr.length + 1, true);
            int length = fArr.length;
            while (i < length) {
                output.writeFloat(fArr[i]);
                i++;
            }
        }

        public float[] read(Kryo kryo, Input input, Class<float[]> cls) {
            int readInt = input.readInt(true);
            if (readInt == 0) {
                return null;
            }
            int i = readInt - 1;
            float[] fArr = new float[i];
            for (int i2 = 0; i2 < i; i2++) {
                fArr[i2] = input.readFloat();
            }
            return fArr;
        }

        public float[] copy(Kryo kryo, float[] fArr) {
            float[] fArr2 = new float[fArr.length];
            System.arraycopy(fArr, 0, fArr2, 0, fArr2.length);
            return fArr2;
        }
    }

    public static class IntArraySerializer extends Serializer<int[]> {
        public IntArraySerializer() {
            setAcceptsNull(true);
        }

        public void write(Kryo kryo, Output output, int[] iArr) {
            if (iArr == null) {
                output.writeByte((byte) 0);
                return;
            }
            output.writeInt(iArr.length + 1, true);
            for (int writeInt : iArr) {
                output.writeInt(writeInt, false);
            }
        }

        public int[] read(Kryo kryo, Input input, Class<int[]> cls) {
            int readInt = input.readInt(true);
            if (readInt == 0) {
                return null;
            }
            int i = readInt - 1;
            int[] iArr = new int[i];
            for (int i2 = 0; i2 < i; i2++) {
                iArr[i2] = input.readInt(false);
            }
            return iArr;
        }

        public int[] copy(Kryo kryo, int[] iArr) {
            int[] iArr2 = new int[iArr.length];
            System.arraycopy(iArr, 0, iArr2, 0, iArr2.length);
            return iArr2;
        }
    }

    public static class LongArraySerializer extends Serializer<long[]> {
        public LongArraySerializer() {
            setAcceptsNull(true);
        }

        public void write(Kryo kryo, Output output, long[] jArr) {
            if (jArr == null) {
                output.writeByte((byte) 0);
                return;
            }
            output.writeInt(jArr.length + 1, true);
            for (long writeLong : jArr) {
                output.writeLong(writeLong, false);
            }
        }

        public long[] read(Kryo kryo, Input input, Class<long[]> cls) {
            int readInt = input.readInt(true);
            if (readInt == 0) {
                return null;
            }
            int i = readInt - 1;
            long[] jArr = new long[i];
            for (int i2 = 0; i2 < i; i2++) {
                jArr[i2] = input.readLong(false);
            }
            return jArr;
        }

        public long[] copy(Kryo kryo, long[] jArr) {
            long[] jArr2 = new long[jArr.length];
            System.arraycopy(jArr, 0, jArr2, 0, jArr2.length);
            return jArr2;
        }
    }

    public static class ObjectArraySerializer extends Serializer<Object[]> {
        private boolean elementsAreSameType;
        private boolean elementsCanBeNull = true;

        public ObjectArraySerializer() {
            setAcceptsNull(true);
        }

        public void write(Kryo kryo, Output output, Object[] objArr) {
            int i = 0;
            if (objArr == null) {
                output.writeByte((byte) 0);
                return;
            }
            output.writeInt(objArr.length + 1, true);
            Class componentType = objArr.getClass().getComponentType();
            if (this.elementsAreSameType || Modifier.isFinal(componentType.getModifiers())) {
                Serializer serializer = kryo.getSerializer(componentType);
                int length = objArr.length;
                while (i < length) {
                    if (this.elementsCanBeNull) {
                        kryo.writeObjectOrNull(output, objArr[i], serializer);
                    } else {
                        kryo.writeObject(output, objArr[i], serializer);
                    }
                    i++;
                }
                return;
            }
            int length2 = objArr.length;
            while (i < length2) {
                kryo.writeClassAndObject(output, objArr[i]);
                i++;
            }
        }

        public Object[] read(Kryo kryo, Input input, Class<Object[]> cls) {
            int i = 0;
            int readInt = input.readInt(true);
            if (readInt == 0) {
                return null;
            }
            Object[] objArr = (Object[]) Array.newInstance(cls.getComponentType(), readInt - 1);
            kryo.reference(objArr);
            Class componentType = objArr.getClass().getComponentType();
            if (this.elementsAreSameType || Modifier.isFinal(componentType.getModifiers())) {
                Serializer serializer = kryo.getSerializer(componentType);
                int length = objArr.length;
                while (i < length) {
                    if (this.elementsCanBeNull) {
                        objArr[i] = kryo.readObjectOrNull(input, componentType, serializer);
                    } else {
                        objArr[i] = kryo.readObject(input, componentType, serializer);
                    }
                    i++;
                }
                return objArr;
            }
            int length2 = objArr.length;
            while (i < length2) {
                objArr[i] = kryo.readClassAndObject(input);
                i++;
            }
            return objArr;
        }

        public Object[] copy(Kryo kryo, Object[] objArr) {
            Object[] objArr2 = (Object[]) Array.newInstance(objArr.getClass().getComponentType(), objArr.length);
            System.arraycopy(objArr, 0, objArr2, 0, objArr2.length);
            return objArr2;
        }

        public void setElementsCanBeNull(boolean z) {
            this.elementsCanBeNull = z;
        }

        public void setElementsAreSameType(boolean z) {
            this.elementsAreSameType = z;
        }
    }

    public static class ShortArraySerializer extends Serializer<short[]> {
        public ShortArraySerializer() {
            setAcceptsNull(true);
        }

        public void write(Kryo kryo, Output output, short[] sArr) {
            int i = 0;
            if (sArr == null) {
                output.writeByte((byte) 0);
                return;
            }
            output.writeInt(sArr.length + 1, true);
            int length = sArr.length;
            while (i < length) {
                output.writeShort(sArr[i]);
                i++;
            }
        }

        public short[] read(Kryo kryo, Input input, Class<short[]> cls) {
            int readInt = input.readInt(true);
            if (readInt == 0) {
                return null;
            }
            int i = readInt - 1;
            short[] sArr = new short[i];
            for (int i2 = 0; i2 < i; i2++) {
                sArr[i2] = input.readShort();
            }
            return sArr;
        }

        public short[] copy(Kryo kryo, short[] sArr) {
            short[] sArr2 = new short[sArr.length];
            System.arraycopy(sArr, 0, sArr2, 0, sArr2.length);
            return sArr2;
        }
    }

    public static class StringArraySerializer extends Serializer<String[]> {
        public StringArraySerializer() {
            setAcceptsNull(true);
        }

        public void write(Kryo kryo, Output output, String[] strArr) {
            int i = 0;
            if (strArr == null) {
                output.writeByte((byte) 0);
                return;
            }
            output.writeInt(strArr.length + 1, true);
            int length = strArr.length;
            while (i < length) {
                output.writeString(strArr[i]);
                i++;
            }
        }

        public String[] read(Kryo kryo, Input input, Class<String[]> cls) {
            int readInt = input.readInt(true);
            if (readInt == 0) {
                return null;
            }
            int i = readInt - 1;
            String[] strArr = new String[i];
            for (int i2 = 0; i2 < i; i2++) {
                strArr[i2] = input.readString();
            }
            return strArr;
        }

        public String[] copy(Kryo kryo, String[] strArr) {
            String[] strArr2 = new String[strArr.length];
            System.arraycopy(strArr, 0, strArr2, 0, strArr2.length);
            return strArr2;
        }
    }
}
