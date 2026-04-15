package org.apache.james.mime4j.storage;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;

public class CipherStorageProvider extends AbstractStorageProvider {
    private final String algorithm;
    private final StorageProvider backend;
    private final KeyGenerator keygen;

    private static final class CipherStorage implements Storage {
        private final String algorithm;
        private Storage encrypted;
        private final SecretKeySpec skeySpec;

        public CipherStorage(Storage encrypted, String algorithm, SecretKeySpec skeySpec) {
            this.encrypted = encrypted;
            this.algorithm = algorithm;
            this.skeySpec = skeySpec;
        }

        public void delete() {
            if (this.encrypted != null) {
                this.encrypted.delete();
                this.encrypted = null;
            }
        }

        public InputStream getInputStream() throws IOException {
            if (this.encrypted == null) {
                throw new IllegalStateException("storage has been deleted");
            }
            try {
                Cipher cipher = Cipher.getInstance(this.algorithm);
                cipher.init(2, this.skeySpec);
                return new CipherInputStream(this.encrypted.getInputStream(), cipher);
            } catch (GeneralSecurityException e) {
                throw ((IOException) new IOException().initCause(e));
            }
        }
    }

    private static final class CipherStorageOutputStream extends StorageOutputStream {
        private final String algorithm;
        private final CipherOutputStream cipherOut;
        private final SecretKeySpec skeySpec;
        private final StorageOutputStream storageOut;

        public CipherStorageOutputStream(StorageOutputStream out, String algorithm, SecretKeySpec skeySpec) throws IOException {
            try {
                this.storageOut = out;
                this.algorithm = algorithm;
                this.skeySpec = skeySpec;
                Cipher cipher = Cipher.getInstance(algorithm);
                cipher.init(1, skeySpec);
                this.cipherOut = new CipherOutputStream(out, cipher);
            } catch (GeneralSecurityException e) {
                throw ((IOException) new IOException().initCause(e));
            }
        }

        public void close() throws IOException {
            super.close();
            this.cipherOut.close();
        }

        /* access modifiers changed from: protected */
        public void write0(byte[] buffer, int offset, int length) throws IOException {
            this.cipherOut.write(buffer, offset, length);
        }

        /* access modifiers changed from: protected */
        public Storage toStorage0() throws IOException {
            return new CipherStorage(this.storageOut.toStorage(), this.algorithm, this.skeySpec);
        }
    }

    public CipherStorageProvider(StorageProvider backend) {
        this(backend, "Blowfish");
    }

    public CipherStorageProvider(StorageProvider backend, String algorithm) {
        if (backend == null) {
            throw new IllegalArgumentException();
        }
        try {
            this.backend = backend;
            this.algorithm = algorithm;
            this.keygen = KeyGenerator.getInstance(algorithm);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public StorageOutputStream createStorageOutputStream() throws IOException {
        return new CipherStorageOutputStream(this.backend.createStorageOutputStream(), this.algorithm, getSecretKeySpec());
    }

    private SecretKeySpec getSecretKeySpec() {
        return new SecretKeySpec(this.keygen.generateKey().getEncoded(), this.algorithm);
    }
}
