package org.crypto;

import java.security.NoSuchAlgorithmException;
import java.sql.Date;
import java.time.Instant;
import java.util.List;

public class Main {


    public static void main(String[] args) throws NoSuchAlgorithmException {
        // Lets create our participants
        Identity jacksIdentity = new Identity("Jack");
        Identity amysIdentity  = new Identity("Amy");
        Identity billsIdentity  = new Identity("Bill");

        // Instantiate the node with initial balance
        Node mainNode = new Node(jacksIdentity, 75);
        // Let's carry out the first transaction
        // Jack sends 25 to Amy and 25 to Zia
        send(jacksIdentity, amysIdentity, mainNode, 50);
        // Amy requests 25 to be sent to her. she supplies the public key in order for the current o
        // owner to transfer ownership to that public key.
        send(amysIdentity, billsIdentity, mainNode, 25);

        System.out.printf("Jacks balance is %s", mainNode.getBalance(jacksIdentity.getPublicKey()));
        System.out.printf("Amy balance is %s", mainNode.getBalance(amysIdentity.getPublicKey()));
        System.out.printf("Zia balance is %s", mainNode.getBalance(billsIdentity.getPublicKey()));
    }

    private static void send(Identity from, Identity to, Node mainNode, int amount) {
        byte[] prevTxId = getLastTransaction(mainNode, from);
        byte[] sig = from.sign(prevTxId, to.getPublicKey().getEncoded(), amount);
        // create a transaction to be stored
        mainNode.saveNewTx(new Transaction(
                prevTxId,
                sig,
                from.getPublicKey(),
                to.getPublicKey(),
                amount, Date.from(Instant.now())));
    }

    private static byte[] getLastTransaction(Node node, Identity identity) {
        List<Transaction> txsForIdentity = node.getTxsForIdentity(identity);
        return txsForIdentity.get(txsForIdentity.size() - 1).thisTxId();
    }
}
