package com.mosambee.notification_sdk.backup_data;

import android.util.Log;

import org.apache.commons.lang3.StringUtils;

import java.security.Key;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import Decoder.BASE64Decoder;
import Decoder.BASE64Encoder;


public class AESEncryptDecryptOld {

    private static final String ENCRYPTION_KEY = "1234561231234564";
    private static final String ENCRYPTION_IV = "1234561231234564";

    public static String getAESEncryptedString(String plainText) {
        String aesEncryptedString = null;
        try {
            if (StringUtils.isNotEmpty(plainText)) {
                aesEncryptedString = encrypt(plainText);
            }
        } catch (Exception e) {
            Log.d("getAESEncryptedString :" , e.getMessage());
        }
        return aesEncryptedString;
    }
    public static String getAESDecryptedString(String encryptedText) {
        String decryptedText = StringUtils.EMPTY;
        try {
            if (StringUtils.isNotEmpty(encryptedText)) {
                decryptedText = decrypt(encryptedText);
            }
        } catch (Exception e) {
            Log.d("getAESDecryptedString :" , e.getMessage());
        }
        return decryptedText;
    }

    private static String encrypt(String data) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, makeKey(), makeIv());
            byte[] encVal = cipher.doFinal(data.getBytes());
            return new BASE64Encoder().encode(encVal);
        } catch (Exception e) {
            Log.d("encrypt :" , e.getMessage());
        }
        return StringUtils.EMPTY;
    }

    private static String decrypt(String encryptedData) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, makeKey(), makeIv());
            byte[] decordedValue = new BASE64Decoder().decodeBuffer(encryptedData);
            byte[] decValue = cipher.doFinal(decordedValue);
            String aaaa = new String(decValue);
            return new String(decValue);
        } catch (Exception e) {
            Log.d("decrypt :" , e.getMessage());
        }
        return StringUtils.EMPTY;
    }

    private static AlgorithmParameterSpec makeIv() {
        try {
            Log.d("makeIv of AESEncrypt : " , String.valueOf(new IvParameterSpec(ENCRYPTION_IV.getBytes("UTF-8"))));
            return new IvParameterSpec(ENCRYPTION_IV.getBytes("UTF-8"));

        } catch (Exception e) {
            Log.d("makeIv of AESEncrypt : " , e.getMessage());
        }
        return null;
    }

    private static Key makeKey() {
        try {
            byte[] key = ENCRYPTION_KEY.getBytes("UTF-8");
            return new SecretKeySpec(key, "AES");
        } catch (Exception e) {
           Log.d("makeKey of AESEncrypt :" , e.getMessage());
        }
        return null;
    }


    public static String byteToHex(byte[] byteData) {
        StringBuilder stringBuilder = new StringBuilder(byteData.length*2);

        for(byte hashByte: byteData) {
            stringBuilder.append(String.format("%02x", hashByte));
        }

        return stringBuilder.toString().toUpperCase();
    }

    public static String byteArrayToHex(byte[] a) {
        StringBuilder sb = new StringBuilder(a.length * 2);
        for(byte b: a)
            sb.append(String.format("%02x", b));
        return sb.toString();
    }
}
