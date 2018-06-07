package cn.vinotec.app.android.comm.utils;

import android.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;

/**
 * Created by kulend@vinotec on 2015/12/01.
 */
public class AESUtil {

    public static String Key = "M8grw/sLXx6hBL2ZJDgE7HrlCdr9MxAW";

    /**
     * 加密
     */
    public static String encrypt(String content){
        try {
            SecretKeySpec skeySpec = getKey(Key);
            byte[] bytContent = content.getBytes("UTF-8");
//            final byte[] iv = "1234567891234567".getBytes("UTF-8");
//            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
            byte[] d = cipher.doFinal(bytContent);
            String encrypedValue = Base64.encodeToString(d, Base64.DEFAULT);
            return encrypedValue;
        }catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 解密
     */
    public static String decrypt(String content) {
        try {
            byte[] decodeStr = Base64.decode(content.getBytes("UTF-8"), Base64.DEFAULT);
            byte[] result = decrypt(decodeStr);
            return new String(result);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static SecretKeySpec getKey(String password) throws UnsupportedEncodingException {
        int keyLength = 256;
        byte[] keyBytes = new byte[keyLength / 8];
        Arrays.fill(keyBytes, (byte) 0x0);
        byte[] passwordBytes = password.getBytes("UTF-8");
        int length = passwordBytes.length < keyBytes.length ? passwordBytes.length : keyBytes.length;
        System.arraycopy(passwordBytes, 0, keyBytes, 0, length);
        SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");
        return key;
    }

    private static byte[] decrypt(byte[] encrypted)
            throws Exception {
        SecretKeySpec skeySpec = getKey(Key);
        //final byte[] iv = "1234567891234567".getBytes("UTF8");
        //IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, skeySpec);
        byte[] decrypted = cipher.doFinal(encrypted);
        return decrypted;
    }
}
