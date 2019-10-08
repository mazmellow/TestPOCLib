package com.mazmellow.testpoclib;

import android.text.TextUtils;
import android.util.Base64;

import org.abstractj.kalium.crypto.Box;
import org.abstractj.kalium.crypto.Random;
import org.abstractj.kalium.keys.PrivateKey;
import org.abstractj.kalium.keys.PublicKey;

public class EncryptionHelper {

    public EncryptData encrypt(String input, String privateKey, String publicKey) throws Exception{

        if(TextUtils.isEmpty(privateKey)){
            throw new Exception("Private key not found.");
        }
        if(TextUtils.isEmpty(publicKey)) {
            throw new Exception("Public key not found.");
        }

        byte[] privateByte = Base64.decode(privateKey, Base64.NO_WRAP);
        byte[] publicByte = Base64.decode(publicKey, Base64.NO_WRAP);
        byte[] nonceByte = new Random().randomBytes(24);
        byte[] paramsByte = input.getBytes();

        Box box = new Box(new PublicKey(publicByte), new PrivateKey(privateByte));
        byte[] cipherByte = box.encrypt(nonceByte, paramsByte);

        String cipher = Base64.encodeToString(cipherByte, Base64.NO_WRAP);
        String nonce = Base64.encodeToString(nonceByte, Base64.NO_WRAP);

        EncryptData encryptData = new EncryptData(cipher, nonce);

        return encryptData;
    }

    public String decrypt(EncryptData encryptData, String publicKey, String privateKey) {

        String cipher = encryptData.getCipher();
        String nonce = encryptData.getNonce();

        byte[] privateByte = Base64.decode(privateKey, Base64.NO_WRAP);
        byte[] publicByte = Base64.decode(publicKey, Base64.NO_WRAP);

        byte[] cipherByte = Base64.decode(cipher, Base64.NO_WRAP);
        byte[] nonceByte = Base64.decode(nonce, Base64.NO_WRAP);

        Box box = new Box(new PublicKey(publicByte), new PrivateKey(privateByte));
        byte[] paramsByte = box.decrypt(nonceByte, cipherByte);

        String output = new String(paramsByte);

        return output;
    }
}
