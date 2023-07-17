package org.crypto;

import java.security.PublicKey;
import java.sql.Date;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Node {
    private final List<Transaction> txs;

    public Node(Identity identity, int amount) {
        this.txs = new ArrayList<>();
        // create genesis tx
        txs.add(new Transaction(String.valueOf(0).getBytes(),
                String.valueOf(0).getBytes(),
                null,
                identity.getPublicKey(), amount,
                Date.from(Instant.now())));
    }

    public Transaction getLastTx() {
        return txs.get(txs.size() - 1);
    }

    public List<Transaction> getTxsForIdentity(Identity identity) {
        return txs.stream()
                .filter(x -> Utils.BytesToHex(x.to().getEncoded())
                        .equals(Utils.BytesToHex(identity.getPublicKey().getEncoded())))
                .sorted(Comparator.comparing(Transaction::date))
                .toList();
    }

    public void saveNewTx(Transaction newTx) {
        txs.add(newTx);
    }

    public int getBalance(PublicKey publicKey) {
        return 0;
    }
}
