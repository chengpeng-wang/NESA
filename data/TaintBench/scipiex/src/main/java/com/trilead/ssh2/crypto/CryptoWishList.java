package com.trilead.ssh2.crypto;

import com.trilead.ssh2.crypto.cipher.BlockCipherFactory;
import com.trilead.ssh2.crypto.digest.MAC;
import com.trilead.ssh2.transport.KexManager;

public class CryptoWishList {
    public String[] c2s_enc_algos = BlockCipherFactory.getDefaultCipherList();
    public String[] c2s_mac_algos = MAC.getMacList();
    public String[] kexAlgorithms = KexManager.getDefaultKexAlgorithmList();
    public String[] s2c_enc_algos = BlockCipherFactory.getDefaultCipherList();
    public String[] s2c_mac_algos = MAC.getMacList();
    public String[] serverHostKeyAlgorithms = KexManager.getDefaultServerHostkeyAlgorithmList();
}
