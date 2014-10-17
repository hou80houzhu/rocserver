package com.rocui.util.lib;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Md5 {

    public static String getMD5Str(String str) {
        if (str == null) {
            return null;
        }
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(str.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            System.exit(-1);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        byte[] byteArray = messageDigest.digest();
        StringBuffer md5StrBuff = new StringBuffer();
        for (int i = 0; i < byteArray.length; i++) {
            if (Integer.toHexString(0xFF & byteArray[i]).length() == 1) {
                md5StrBuff.append("0").append(Integer.toHexString(0xFF & byteArray[i]));
            } else {
                md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
            }
        }
        return getNewMd5(md5StrBuff.toString());
    }

    public static String getNewMd5(String md5) {
        StringBuffer newmd5 = new StringBuffer();
        newmd5.append(md5.substring(25, 26));
        newmd5.append(md5.substring(19, 20));
        newmd5.append(md5.substring(2, 3));
        newmd5.append(md5.substring(15, 16));
        newmd5.append(md5.substring(6, 7));
        newmd5.append(md5.substring(21, 22));
        newmd5.append(md5.substring(4, 5));
        newmd5.append(md5.substring(7, 8));
        newmd5.append(md5.substring(16, 17));
        newmd5.append(md5.substring(31, 32));
        newmd5.append(md5.substring(27, 28));
        newmd5.append(md5.substring(24, 25));
        newmd5.append(md5.substring(13, 14));
        newmd5.append(md5.substring(12, 13));
        newmd5.append(md5.substring(30, 31));
        newmd5.append(md5.substring(3, 4));
        newmd5.append(md5.substring(8, 9));
        newmd5.append(md5.substring(22, 23));
        newmd5.append(md5.substring(28, 29));
        newmd5.append(md5.substring(1, 2));
        newmd5.append(md5.substring(23, 24));
        newmd5.append(md5.substring(5, 6));
        newmd5.append(md5.substring(17, 18));
        newmd5.append(md5.substring(20, 21));
        newmd5.append(md5.substring(11, 12));
        newmd5.append(md5.substring(0, 1));
        newmd5.append(md5.substring(29, 30));
        newmd5.append(md5.substring(10, 11));
        newmd5.append(md5.substring(18, 19));
        newmd5.append(md5.substring(26, 27));
        newmd5.append(md5.substring(14, 15));
        newmd5.append(md5.substring(9, 10));
        return newmd5.toString();
    }
}
