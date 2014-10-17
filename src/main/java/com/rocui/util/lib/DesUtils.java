/**
 * DesUtils.java com.kp.im.common Function： TODO
 *
 * ver date author ────────────────────────────────── 2011-11-17 hou80houzhu
 *
 * Copyright (c) 2011, Kenesphone All Rights Reserved.
 */
package com.rocui.util.lib;

import java.security.Key;
import java.security.Security;

import javax.crypto.Cipher;

/**
 * ClassName:DesUtils
 *
 * @author hou80houzhu
 * @version v 1.0.0.0
 * @Date 2011-11-17
 *
 * @see
 */
public class DesUtils {

    public static String SPREAD_APK = "E28CA9F432E5F4D428D3F85A0273BD5D";
    public static String TOKEN_ALL = "CA432E58DF2A73BE2D5D8F4D423F8590";
    public static String TOKEN_IMGAPK = "675531887EEA5AB94443B7D6C4AFF3E2";
    public static String GOLD_ADD = "D727A3E52A822C1409AC8D2D9D02CAA7";
    private static Cipher encryptCipher = null;
    private static Cipher decryptCipher = null;

    public DesUtils(String strKey) {
        try {
            Security.addProvider(new com.sun.crypto.provider.SunJCE());
            Key key = getKey(strKey.getBytes());

            encryptCipher = Cipher.getInstance("DES");
            encryptCipher.init(Cipher.ENCRYPT_MODE, key);

            decryptCipher = Cipher.getInstance("DES");
            decryptCipher.init(Cipher.DECRYPT_MODE, key);
        } catch (Exception e) {
        }
    }

    private static String byteArr2HexStr(byte[] arrB) throws Exception {
        int iLen = arrB.length;
        StringBuffer sb = new StringBuffer(iLen * 2);
        for (int i = 0; i < iLen; i++) {
            int intTmp = arrB[i];
            while (intTmp < 0) {
                intTmp = intTmp + 256;
            }
            if (intTmp < 16) {
                sb.append("0");
            }
            sb.append(Integer.toString(intTmp, 16));
        }
        return sb.toString();
    }

    private static byte[] hexStr2ByteArr(String strIn) throws Exception {
        byte[] arrB = strIn.getBytes();
        int iLen = arrB.length;
        byte[] arrOut = new byte[iLen / 2];
        for (int i = 0; i < iLen; i = i + 2) {
            String strTmp = new String(arrB, i, 2);
            arrOut[i / 2] = (byte) Integer.parseInt(strTmp, 16);
        }
        return arrOut;
    }

    private Key getKey(byte[] arrBTmp) throws Exception {
        byte[] arrB = new byte[8];
        for (int i = 0; i < arrBTmp.length && i < arrB.length; i++) {
            arrB[i] = arrBTmp[i];
        }
        Key key = new javax.crypto.spec.SecretKeySpec(arrB, "DES");
        return key;
    }

    private byte[] encrypt(byte[] arrB) throws Exception {
        return encryptCipher.doFinal(arrB);
    }

    private byte[] decrypt(byte[] arrB) throws Exception {
        return decryptCipher.doFinal(arrB);
    }

    public String encrypt(String strIn) throws Exception {
        return byteArr2HexStr(encrypt(strIn.getBytes()));
    }

    public String decrypt(String strIn) {
        try {
            return new String(decrypt(hexStr2ByteArr(strIn)));
        } catch (Exception e) {
            return "";
        }
    }
}
