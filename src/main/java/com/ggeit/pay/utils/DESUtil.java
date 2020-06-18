package com.ggeit.pay.utils;

import java.util.Base64;

import javax.crypto.Cipher;

import javax.crypto.KeyGenerator;

import javax.crypto.NoSuchPaddingException;

import javax.crypto.SecretKey;

import javax.crypto.spec.SecretKeySpec;

public class DESUtil {

    

    /*

     * 生成密钥

     */

    public static byte[] initKey() throws Exception{

        KeyGenerator keyGen = KeyGenerator.getInstance("DES");

        keyGen.init(56);

        SecretKey secretKey = keyGen.generateKey();
        
        System.out.println("生成密钥：" + secretKey.getEncoded());

        return secretKey.getEncoded();

    }

 
    /*

     * DES 加密

     */

    public static byte[] encrypt(byte[] data, byte[] key) throws Exception{

        SecretKey secretKey = new SecretKeySpec(key, "DES");
      

        

        Cipher cipher = Cipher.getInstance("DES");

        cipher.init(Cipher.ENCRYPT_MODE, secretKey);

        byte[] cipherBytes = cipher.doFinal(data);

       return cipherBytes;
        //return Base64.getEncoder().encodeToString(cipherBytes);

    }

    

    

    /*

     * DES 解密

     */

    public static byte[] decrypt(byte[] data, byte[] key) throws Exception{

        SecretKey secretKey = new SecretKeySpec(key, "DES");

        

        Cipher cipher = Cipher.getInstance("DES");

        cipher.init(Cipher.DECRYPT_MODE, secretKey);

        byte[] plainBytes = cipher.doFinal(data);

        return plainBytes;

    }

 

    //Test

    public static void main(String[] args) throws Exception {

    byte[] desKey = DESUtil.initKey();
    System.out.println("加密密钥desKey=" + desKey);

        //System.out.println("DES KEY : " + BytesToHex.fromBytesToHex(desKey));
    	String data1 = "index?price=0.01&remark=挂号&paytype=0001&orderid=20190924162512341234";
    	String data2 = "1";
    	
        byte[] desResult1 = DESUtil.encrypt(data1.getBytes(), desKey);
        byte[] desResult2 = DESUtil.encrypt(data2.getBytes(), desKey);
        
       System.out.println(data1 + ">>>DES 加密结果>>>" + desResult1);
       System.out.println(data2 + ">>>DES 加密结果>>>" + desResult2);
        

//        byte[] desPlain1 = DESUtil.decrypt(desResult1, desKey);
//        byte[] desPlain2 = DESUtil.decrypt(desResult2, desKey);
        
//        System.out.println(data1 + ">>>DES 解密结果>>>" + new String(desPlain1));
//        System.out.println(data2 + ">>>DES 解密结果>>>" + new String(desPlain2));
    }

}
