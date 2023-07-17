package org.crypto;

public class Utils {
    public static String BytesToHex(byte[] myBlockHash) {
        StringBuilder hexString = new StringBuilder(2 * myBlockHash.length);
        for (byte blockHash : myBlockHash) {
            String hex = Integer.toHexString(0xff & blockHash);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
