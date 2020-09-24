package com.zcshou.utils;

import org.jetbrains.annotations.NotNull;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import java.nio.charset.StandardCharsets;
import java.util.Random;


public class AESUtils {
    /*   AES秘钥支持128bit/192bit/256bit三种长度的秘钥，一个字节等于8bit，
     *   因此支持生成的字符串的长度应该是 16/24/32
     * */

    //  16 字节 AES秘钥长度（128bit）
    public static final int KEY_LENGTH_16 = 16;
    //  24 字节 AES秘钥长度（192bit）
    public static final int KEY_LENGTH_24 = 24;
    //  32 字节 AES秘钥长度（256bit）
    public static final int KEY_LENGTH_32 = 32;

    // 实际上，“ RSA / ECB / PKCS1Padding”未实现ECB模式加密。 它应该被称为“ RSA / None / PKCS1Padding”，
    // 因为它只能用于加密单个明文块（或者实际上是一个秘密密钥）。 这只是Sun / Oracle的命名错误。
    private static final String cipherMode = "AES/None/PKCS5Padding";//算法/模式/补码方式


//    public static void main(String[] args) {
//
//        /*构建一个随机密码*/
//        String key = getRandomKey(24);
//        System.out.println("随机生成的key：" + key);
//
//        String data = "{'fig':1,'message':'登录成功'}";
//
//        try {
//            String encriptData = AESUtils.encrypt(data, key);
//            System.out.println("加密后的数据：" + encriptData);
//
//            String decryptData = decrypt(encriptData, key);
//
//            System.out.println("解密后的数据：" + decryptData);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }


    /**
     * @param length 需要生成的字符串长度
     * @return 随机生成的字符串
     */
    public static String getRandomKey(int length) {

        if (length != KEY_LENGTH_16 && length != KEY_LENGTH_24 && length != KEY_LENGTH_32) {
            System.out.println("长度必须为16/24/32");
            return null;
        }

        String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(62);
            stringBuilder.append(str.charAt(number));
        }
        return stringBuilder.toString();

    }


    /**
     * @param data 需要加密的数据
     * @param key  加密使用的key
     * @return 加密后的数据(Base64编码)
     * @throws Exception 异常
     */
    public static String encrypt(String data, @NotNull String key) throws Exception {

        int length = key.length();
        if (length != KEY_LENGTH_16 && length != KEY_LENGTH_24 && length != KEY_LENGTH_32) {
            System.out.println("长度必须为16/24/32");
            return null;
        }

        byte[] raw = key.getBytes(StandardCharsets.UTF_8);
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance(cipherMode);
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        byte[] encrypted = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));

        return Base64.encode(encrypted);
    }


    /**
     * @param data 需要解密的数据
     * @param key  解密用的key
     * @return 解密后的数据
     */
    public static String decrypt(String data, @NotNull String key) {
        try {
            int length = key.length();
            if (length != KEY_LENGTH_16 && length != KEY_LENGTH_24 && length != KEY_LENGTH_32) {
                System.out.println("长度必须为16/24/32");
                return null;
            }

            byte[] raw = key.getBytes(StandardCharsets.UTF_8);
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance(cipherMode);
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
            byte[] encrypted = Base64.decode(data);//先用base64解密
            try {
                byte[] original = cipher.doFinal(encrypted);
                return new String(original, StandardCharsets.UTF_8);
            } catch (Exception e) {
                System.out.println(e.toString());
                return null;
            }
        } catch (Exception ex) {
            System.out.println(ex.toString());
            return null;
        }
    }

}