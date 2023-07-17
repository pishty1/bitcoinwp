package org.crypto;


import java.security.*;
import java.sql.Date;
import java.time.Instant;

public class Identity {
    private final PrivateKey privateKey;
    private final PublicKey publicKey;
    private final String name;

    public Identity(String name) throws NoSuchAlgorithmException {
        this.name = name;
        KeyPairGenerator sda = KeyPairGenerator.getInstance("DSA");
        sda.initialize(2048);
        KeyPair keyPair = sda.generateKeyPair();
        this.privateKey = keyPair.getPrivate();
        this.publicKey = keyPair.getPublic();
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public String getName() {
        return name;
    }

    public byte[] sign(byte[] txId, byte[] publicKey, int amount) {
        try {
            Signature sha256withDSA = Signature.getInstance("SHA256withDSA");
            sha256withDSA.initSign(this.privateKey);

            sha256withDSA.update(txId);
            sha256withDSA.update(publicKey);
            sha256withDSA.update(String.valueOf(amount).getBytes());

            return sha256withDSA.sign();
        } catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException e) {
            throw new RuntimeException(e);
        }
    }

    public Transaction createTransaction(Node node, Identity to, int amount) {
        // get the last transaction that assigned the coin
        Transaction lastTx = node.getLastTx();
        byte[] sig = sign(lastTx.thisTxId(), to.getPublicKey().getEncoded(), amount);
        return new Transaction(
                lastTx.thisTxId(),
                sig,
                this.getPublicKey(),
                to.getPublicKey(),
                amount, Date.from(Instant.now()));
    }

    public boolean verify(byte[] transaction, byte[] fromPublicKey) {

        return false;
    }
}
