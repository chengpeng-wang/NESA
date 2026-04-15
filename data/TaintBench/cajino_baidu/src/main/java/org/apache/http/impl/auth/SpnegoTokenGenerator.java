package org.apache.http.impl.auth;

import java.io.IOException;

public interface SpnegoTokenGenerator {
    byte[] generateSpnegoDERObject(byte[] bArr) throws IOException;
}
