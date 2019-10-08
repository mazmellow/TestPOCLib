package com.mazmellow.testpoclib;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MainActivity extends AppCompatActivity {

    private final String TAG = MainActivity.class.getSimpleName();

    private String pathText = "";

    private Keypair keypair;
    private EncryptionHelper encryptionHelper;
    private JSONObject logicJson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Tutorial:  https://www.geeksforgeeks.org/reflection-in-java/

        findViewById(R.id.btnEncrypt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                encryptJsonToText();
            }
        });

        findViewById(R.id.btnDecrypt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                decryptTextToJson();
            }
        });

        findViewById(R.id.btnStart).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    start();
                }catch (IllegalAccessException e) {
                    Log.e(TAG, "IllegalAccessException: "+e.getMessage());
                }catch (InvocationTargetException e) {
                    Log.e(TAG, "InvocationTargetException: "+e.getMessage());
                }catch (NoSuchMethodException e) {
                    Log.e(TAG, "NoSuchMethodException: " + e.getMessage());
                }
            }
        });


    }

    // This function is not included in project
    private void encryptJsonToText() {

        keypair = new Keypair();
        encryptionHelper = new EncryptionHelper();

        try {
            //Read file
            InputStream is = getAssets().open("logic.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String jsonText = new String(buffer, "UTF-8");

            //Encrypt file
            EncryptData encryptData = encryptionHelper.encrypt(jsonText, keypair.getPrivateKey(), keypair.getPublicKey());
            String result = encryptData.getCipher()+"."+encryptData.getNonce();
            Log.i(TAG, "result: "+result);
            Log.i(TAG, "public key: "+keypair.getPublicKey());
            Log.i(TAG, "private key: "+keypair.getPrivateKey());

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void decryptTextToJson() {

        try {
            //Read file
            AssetManager assetManager = getAssets();

            InputStream isCipher = assetManager.open("logic.txt");
            byte[] bufferCipher = new byte[isCipher.available()];
            isCipher.read(bufferCipher);
            isCipher.close();
            String cipherNonceText = new String(bufferCipher, "UTF-8");
            Log.i(TAG, "cipherNonceText: "+cipherNonceText);

            if(cipherNonceText.indexOf(".")==-1){
                throw new Exception("wrong logic file.");
            }
            String cipher = cipherNonceText.substring(0, cipherNonceText.indexOf("."));
            String nonce = cipherNonceText.substring(cipherNonceText.indexOf(".")+1);
            EncryptData encryptData = new EncryptData(cipher, nonce);

            InputStream isPublic = assetManager.open("publickey.txt");
            byte[] bufferPublic = new byte[isPublic.available()];
            isPublic.read(bufferPublic);
            isPublic.close();
            String publicKey = new String(bufferPublic, "UTF-8");

            InputStream isPrivate = assetManager.open("privatekey.txt");
            byte[] bufferPrivate = new byte[isPrivate.available()];
            isPrivate.read(bufferPrivate);
            isPrivate.close();
            String privateKey = new String(bufferPrivate, "UTF-8");

            Log.i(TAG, "public key: "+publicKey);
            Log.i(TAG, "private key: "+privateKey);

            String jsonText = new EncryptionHelper().decrypt(encryptData, publicKey, privateKey);
            logicJson = new JSONObject(jsonText);
            Log.i(TAG,"Success Decrypt");

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void start() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException/*, NoSuchFieldException*/ {
        if(logicJson == null){
            Log.w(TAG, "logicJson == null");
            return;
        }

        try {
            callMethod(logicJson.getString("start"));

            JSONArray jsonArray = logicJson.getJSONArray("flow1");
            for(int i=0; i<jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                callMethod(jsonObject.getString("name"));
            }

        }catch (JSONException e) {

        }

    }

    private void callMethod(String methodName) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException{
        Logic logic = new Logic();
        Class logicClass = logic.getClass();

        Method methodcall1 = logicClass.getDeclaredMethod(methodName);
//            Object output = methodcall1.invoke(obj, 9999);
        Object output = methodcall1.invoke(logic);
        if(output != null) Log.d(TAG, "output start: "+output);

        // Getter-Setter
//        Field field = cls.getDeclaredField("somefield");
//        field.setAccessible(true);
//        field.set(obj, "Logic value");
    }
}
