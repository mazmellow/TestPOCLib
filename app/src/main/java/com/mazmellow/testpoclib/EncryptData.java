package com.mazmellow.testpoclib;

public class EncryptData {

    private String cipher;
    private String nonce;

    public EncryptData(String cipher, String nonce) {
        this.cipher = cipher;
        this.nonce = nonce;
    }

    public String getCipher() {
        return cipher;
    }

    public void setCipher(String cipher) {
        this.cipher = cipher;
    }

    public String getNonce() {
        return nonce;
    }

    public void setNonce(String nonce) {
        this.nonce = nonce;
    }
}
