package com.trilead.ssh2.crypto.dh;

import com.trilead.ssh2.crypto.digest.HashForSSH2Types;
import com.trilead.ssh2.log.Logger;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.SecureRandom;

public class DhExchange {
    static final BigInteger g = new BigInteger("2");
    private static final Logger log = Logger.getLogger(DhExchange.class);
    static final BigInteger p1 = new BigInteger("179769313486231590770839156793787453197860296048756011706444423684197180216158519368947833795864925541502180565485980503646440548199239100050792877003355816639229553136239076508735759914822574862575007425302077447712589550957937778424442426617334727629299387668709205606050270810842907692932019128194467627007");
    static final BigInteger p14 = new BigInteger("FFFFFFFFFFFFFFFFC90FDAA22168C234C4C6628B80DC1CD129024E088A67CC74020BBEA63B139B22514A08798E3404DDEF9519B3CD3A431B302B0A6DF25F14374FE1356D6D51C245E485B576625E7EC6F44C42E9A637ED6B0BFF5CB6F406B7EDEE386BFB5A899FA5AE9F24117C4B1FE649286651ECE45B3DC2007CB8A163BF0598DA48361C55D39A69163FA8FD24CF5F83655D23DCA3AD961C62F356208552BB9ED529077096966D670C354E4ABC9804F1746C08CA18217C32905E462E36CE3BE39E772C180E86039B2783A2EC07A28FB5C55DF06F4C52C9DE2BCBF6955817183995497CEA956AE515D2261898FA051015728E5A8AACAA68FFFFFFFFFFFFFFFF", 16);
    BigInteger e;
    BigInteger f;
    BigInteger k;
    BigInteger p;
    BigInteger x;

    static {
        String p1_string = "179769313486231590770839156793787453197860296048756011706444423684197180216158519368947833795864925541502180565485980503646440548199239100050792877003355816639229553136239076508735759914822574862575007425302077447712589550957937778424442426617334727629299387668709205606050270810842907692932019128194467627007";
        String p14_string = "FFFFFFFFFFFFFFFFC90FDAA22168C234C4C6628B80DC1CD129024E088A67CC74020BBEA63B139B22514A08798E3404DDEF9519B3CD3A431B302B0A6DF25F14374FE1356D6D51C245E485B576625E7EC6F44C42E9A637ED6B0BFF5CB6F406B7EDEE386BFB5A899FA5AE9F24117C4B1FE649286651ECE45B3DC2007CB8A163BF0598DA48361C55D39A69163FA8FD24CF5F83655D23DCA3AD961C62F356208552BB9ED529077096966D670C354E4ABC9804F1746C08CA18217C32905E462E36CE3BE39E772C180E86039B2783A2EC07A28FB5C55DF06F4C52C9DE2BCBF6955817183995497CEA956AE515D2261898FA051015728E5A8AACAA68FFFFFFFFFFFFFFFF";
    }

    public void init(int group, SecureRandom rnd) {
        this.k = null;
        if (group == 1) {
            this.p = p1;
        } else if (group == 14) {
            this.p = p14;
        } else {
            throw new IllegalArgumentException("Unknown DH group " + group);
        }
        this.x = new BigInteger(this.p.bitLength() - 1, rnd);
        this.e = g.modPow(this.x, this.p);
    }

    public BigInteger getE() {
        if (this.e != null) {
            return this.e;
        }
        throw new IllegalStateException("DhDsaExchange not initialized!");
    }

    public BigInteger getK() {
        if (this.k != null) {
            return this.k;
        }
        throw new IllegalStateException("Shared secret not yet known, need f first!");
    }

    public void setF(BigInteger f) {
        if (this.e == null) {
            throw new IllegalStateException("DhDsaExchange not initialized!");
        } else if (BigInteger.valueOf(0).compareTo(f) >= 0 || this.p.compareTo(f) <= 0) {
            throw new IllegalArgumentException("Invalid f specified!");
        } else {
            this.f = f;
            this.k = f.modPow(this.x, this.p);
        }
    }

    public byte[] calculateH(byte[] clientversion, byte[] serverversion, byte[] clientKexPayload, byte[] serverKexPayload, byte[] hostKey) throws UnsupportedEncodingException {
        HashForSSH2Types hash = new HashForSSH2Types("SHA1");
        if (log.isEnabled()) {
            log.log(90, "Client: '" + new String(clientversion, "ISO-8859-1") + "'");
            log.log(90, "Server: '" + new String(serverversion, "ISO-8859-1") + "'");
        }
        hash.updateByteString(clientversion);
        hash.updateByteString(serverversion);
        hash.updateByteString(clientKexPayload);
        hash.updateByteString(serverKexPayload);
        hash.updateByteString(hostKey);
        hash.updateBigInt(this.e);
        hash.updateBigInt(this.f);
        hash.updateBigInt(this.k);
        return hash.getDigest();
    }
}
