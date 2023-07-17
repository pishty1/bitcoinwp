package org.crypto;

import java.security.PublicKey;
import java.util.Date;

public record Transaction (byte[] previousTxId, byte[] thisTxId, PublicKey from, PublicKey to, int amount, Date date){}
