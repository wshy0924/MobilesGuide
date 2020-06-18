package com.ggeit.pay.utils;

import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;


import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import com.alibaba.druid.util.Base64;

public class DesCipherUtil {

    private DesCipherUtil() {
        throw new AssertionError("No DesCipherUtil instances for you!");
    }

//    static {
//        // add BC provider
//        Security.addProvider(new BouncyCastleProvider());
//    }

    /**
     * 加密
     * 
     * @param encryptText 需要加密的信息
     * @param key 加密密钥
     * @return 加密后Base64编码的字符串
     */
    public static String encrypt(String encryptText, String key) {

        if (encryptText == null || key == null) {
            throw new IllegalArgumentException("encryptText or key must not be null");
        }

        try {
            DESKeySpec desKeySpec = new DESKeySpec(key.getBytes());
            SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey secretKey = secretKeyFactory.generateSecret(desKeySpec);

            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] bytes = cipher.doFinal(encryptText.getBytes(Charset.forName("UTF-8")));
            return Base64.byteArrayToBase64(bytes);
           // return bytes.toString();

        } catch (NoSuchAlgorithmException | InvalidKeyException | InvalidKeySpecException | NoSuchPaddingException
            | BadPaddingException | IllegalBlockSizeException e) {
            throw new RuntimeException("encrypt failed", e);
        }

    }

    /**
     * 解密
     * 
     * @param desResult1 需要解密的信息
     * @param key 解密密钥，经过Base64编码
     * @return 解密后的字符串
     */
    public static  String decrypt(String decryptText, String key) {

        if (decryptText == null || key == null) {
            throw new IllegalArgumentException("decryptText or key must not be null");
        }

        try {
            DESKeySpec desKeySpec = new DESKeySpec(key.getBytes());
            SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey secretKey = secretKeyFactory.generateSecret(desKeySpec);
            System.out.println("加密密钥：" + secretKey);

            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] bytes = cipher.doFinal(Base64.base64ToByteArray(decryptText));
            //byte[] bytes = cipher.doFinal(desResult1);
            return new String(bytes, Charset.forName("UTF-8"));

        } catch (NoSuchAlgorithmException | InvalidKeyException | InvalidKeySpecException | NoSuchPaddingException
            | BadPaddingException | IllegalBlockSizeException e) {
            throw new RuntimeException("decrypt failed", e);
        }
    }
   
    public static void main(String[] args) throws Exception {
    	

            //System.out.println("DES KEY : " + BytesToHex.fromBytesToHex(desKey));
        	String data1 = "index?price=0.01&remark=挂号&paytype=0001&orderid=20190924162512341234";
        	String data2 = "1";
        	
            String desResult1 = DesCipherUtil.encrypt(data1, "[B@31b7dea0");
            String desResult2 = DesCipherUtil.encrypt(data2, "[B@31b7dea0");
            
            System.out.println(data1 + ">>>DES 加密结果>>>" + "\n" + desResult1);
            System.out.println(data2 + ">>>DES 加密结果>>>" + desResult2);
            

            String desPlain1 = DesCipherUtil.decrypt(desResult1, "[B@31b7dea0");
            String desPlain2 = DesCipherUtil.decrypt(desResult2, "[B@31b7dea0");
            
            System.out.println(data1 + ">>>DES 解密结果>>>" + new String(desPlain1));
            System.out.println(data2 + ">>>DES 解密结果>>>" + new String(desPlain2));
        }

}


