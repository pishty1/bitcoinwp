import org.crypto.Identity;
import org.crypto.Node;
import org.crypto.Transaction;
import org.crypto.Utils;
import org.junit.Test;

import java.security.*;

import static org.junit.Assert.assertTrue;

public class KeysTest {

    @Test
    public void testSignMessageAndVerify() throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        MessageDigest messageDigestInstance = MessageDigest.getInstance("SHA-256");
        messageDigestInstance.update("Hello world".getBytes());
        byte[] digest = messageDigestInstance.digest();
        System.out.printf("Hex formatted message digest: %s\n", Utils.BytesToHex(digest));
        // generating the key pairs
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("DSA");
        keyPairGenerator.initialize(2048);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        // Using the private key sign a message

        Signature sigObj = Signature.getInstance("SHA256withDSA");
        // Initialise the signature object
        sigObj.initSign(keyPair.getPrivate());
        // Adding data to the signature object
        byte[] message = "Hello world".getBytes();
        sigObj.update(message);
        byte[] signature = sigObj.sign();

        // Lets verify now
        sigObj.initVerify(keyPair.getPublic());
        sigObj.update(message);
        assertTrue(sigObj.verify(signature));
    }

    @Test
    public void testSendAndVerifyTxs()throws NoSuchAlgorithmException {
        // Lets create our participants
        Identity jacksIdentity = new Identity("Jack");
        Identity amysIdentity  = new Identity("Amy");
        Identity billIdentity  = new Identity("Bill");
        // Instantiate the node with initial balance
        Node node = new Node(jacksIdentity, 75);
        // Let's carry out the first transaction
        // Jack sends 25 to Amy and 25 to Zia
        Transaction transaction = jacksIdentity.createTransaction(node, amysIdentity, 50);
        node.saveNewTx(transaction);
        // let amy verify that jack was the owner of the coin that was sent to her.
        amysIdentity.verify(transaction.thisTxId(), jacksIdentity.getPublicKey().getEncoded());

        // Amy requests 25 to be sent to her. she supplies the public key in order for the current o
        // owner to transfer ownership to that public key.
        Transaction transaction1 = amysIdentity.createTransaction(node, billIdentity, 25);
        node.saveNewTx(transaction1);

        System.out.printf("Jacks balance is %s", node.getBalance(jacksIdentity.getPublicKey()));
        System.out.printf("Amy balance is %s", node.getBalance(amysIdentity.getPublicKey()));
        System.out.printf("Zia balance is %s", node.getBalance(billIdentity.getPublicKey()));
    }

}
