package com.mazmellow.testpoclib;

import android.util.Base64;

import org.abstractj.kalium.keys.KeyPair;

public class Keypair {

    private String publicKey;
    private String privateKey;

    public Keypair() {
        // Generate key
        KeyPair sodiumKeyPair = new KeyPair();
        publicKey = Base64.encodeToString(sodiumKeyPair.getPublicKey().toBytes(), Base64.NO_WRAP);
        privateKey = Base64.encodeToString(sodiumKeyPair.getPrivateKey().toBytes(), Base64.NO_WRAP);
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }
}
