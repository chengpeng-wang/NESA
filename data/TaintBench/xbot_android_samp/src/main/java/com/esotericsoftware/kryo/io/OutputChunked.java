package com.esotericsoftware.kryo.io;

import com.esotericsoftware.kryo.KryoException;
import java.io.IOException;
import java.io.OutputStream;
import org.objectweb.asm.Opcodes;

public class OutputChunked extends Output {
    public OutputChunked() {
        super((int) Opcodes.ACC_STRICT);
    }

    public OutputChunked(int i) {
        super(i);
    }

    public OutputChunked(OutputStream outputStream) {
        super(outputStream, (int) Opcodes.ACC_STRICT);
    }

    public OutputChunked(OutputStream outputStream, int i) {
        super(outputStream, i);
    }

    public void flush() throws KryoException {
        if (position() > 0) {
            try {
                writeChunkSize();
            } catch (IOException e) {
                throw new KryoException(e);
            }
        }
        super.flush();
    }

    private void writeChunkSize() throws IOException {
        int position = position();
        OutputStream outputStream = getOutputStream();
        if ((position & -128) == 0) {
            outputStream.write(position);
            return;
        }
        outputStream.write((position & 127) | 128);
        position >>>= 7;
        if ((position & -128) == 0) {
            outputStream.write(position);
            return;
        }
        outputStream.write((position & 127) | 128);
        position >>>= 7;
        if ((position & -128) == 0) {
            outputStream.write(position);
            return;
        }
        outputStream.write((position & 127) | 128);
        position >>>= 7;
        if ((position & -128) == 0) {
            outputStream.write(position);
            return;
        }
        outputStream.write((position & 127) | 128);
        outputStream.write(position >>> 7);
    }

    public void endChunks() {
        flush();
        try {
            getOutputStream().write(0);
        } catch (IOException e) {
            throw new KryoException(e);
        }
    }
}
