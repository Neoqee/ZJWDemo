package com.demo.demolib.utils;

import android.util.Base64;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class EncodeUtil {

    public static final String clientSecret="F4D8D3BEB05048D29B9ABB6994B2F0AD";

    public static String CreateAccessCode(String rawData, String clientSecret)
    {
        // 随机生成2个字符
        String[] templates = new String[] { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z" };
        int tempLen = templates.length;
        Random random = new Random();

        String randomDigit = templates[random.nextInt(tempLen)]+templates[random.nextInt(tempLen)]; // 相当于templates[random.Next(0, tempLen)+templates[random.Next(0, tempLen)

        String hash="";
        try {
            Mac sha256_HMAC=Mac.getInstance("HmacSHA256");
            String newKey=clientSecret+randomDigit;
            byte[] keyByte = newKey.getBytes(StandardCharsets.UTF_8);
            SecretKeySpec secretKeySpec=new SecretKeySpec(keyByte,"HmacSHA256");
            sha256_HMAC.init(secretKeySpec);
            byte[] bytes=sha256_HMAC.doFinal(rawData.getBytes(StandardCharsets.UTF_8));
//            hash= Base64.encodeToString(bytes,Base64.DEFAULT);
            hash=byteArrayToHexString(bytes);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
//        // 进行HMACSHA256加密并转成字符串，注意字符编码使用UTF-8
//        Encoding encoding = new UTF8Encoding();
//        byte[] keyByte = encoding.GetBytes(clientSecret + randomDigit); // 将APPSECRETKEY与随机数拼接成新加密密钥Key
//        byte[] messageBytes = encoding.GetBytes(rawData);
//        string hexHash = "";
//        using (HMACSHA256 hasher = new HMACSHA256(keyByte))  // 将APPSECRETKEY+随机数作为密钥传输给HMACSHA256
//        {
//            byte[] hashValue = hasher.ComputeHash(messageBytes);
//            foreach (byte b in hashValue)
//            {
//                // 转成16进制的大写字符进行拼接成字符串
//                // X表示转成16进制大写字符串,2表示是两位数（即3会转成03,11会转成0B）
//                hexHash += b.ToString("X2");
//            }
//        }
//
//        return String.Concat(randomDigit, hexHash); // 相当于randomDigit+hexHash
        return randomDigit+hash;
    }

    private static String byteArrayToHexString(byte[] b) {
        StringBuilder hs = new StringBuilder();
        String stmp;
        for (int n = 0; b!=null && n < b.length; n++) {
            stmp = Integer.toHexString(b[n] & 0XFF);
            if (stmp.length() == 1)
                hs.append('0');
            hs.append(stmp);
        }
        return hs.toString().toUpperCase();
    }

}
