package com.trilead.ssh2;

public class ConnectionInfo {
    public String clientToServerCryptoAlgorithm;
    public String clientToServerMACAlgorithm;
    public String keyExchangeAlgorithm;
    public int keyExchangeCounter = 0;
    public byte[] serverHostKey;
    public String serverHostKeyAlgorithm;
    public String serverToClientCryptoAlgorithm;
    public String serverToClientMACAlgorithm;
}
