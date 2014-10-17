package com.rocui.util.base.string;

import com.rocui.util.base.Jdate;
import com.rocui.util.file.Jile;
import com.rocui.util.lib.DesUtils;
import com.rocui.util.lib.Md5;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Date;

public class Jstring {

    private String str;
    private String[] array = new String[]{};
    private String cache;

    private Jstring(String str) {
        this.str = str;
        this.cache = str;
    }

    public static Jstring with(String str) {
        return new Jstring(str);
    }

    public static Jstring with(File file) throws IOException {
        return new Jstring(Jile.with(file).read());
    }

    public Jstring split(String str) {
        this.array = this.str.split(str);
        return this;
    }

    public Jstring each(JstringEach each) {
        if (this.array.length > 0) {
            for (int i = 0; i < this.array.length; i++) {
                boolean isbreak = each.each(this.array[i], i);
                if (isbreak) {
                    break;
                }
            }
        }
        return this;
    }

    public Jstring md5() {
        return new Jstring(Md5.getMD5Str(this.str));
    }

    public int length() {
        return this.str.length();
    }

    public Jstring trim() {
        return new Jstring(this.str.trim());
    }

    public Jstring utf() throws UnsupportedEncodingException {
        return new Jstring(new String(this.str.getBytes("ISO8859_1"), "UTF-8"));
    }

    public boolean equals(Jstring string) {
        return this.str.equals(string.str);
    }

    public boolean equals(String string) {
        return this.str.equals(string);
    }

    public Jstring append(String str) {
        return new Jstring(this.str + str);
    }

    public Jstring replace(char a, char b) {
        return new Jstring(this.str.replace(a, b));
    }

    public Jstring replace(String a, String b) {
        return new Jstring(this.str.replaceAll(a, b));
    }

    public String noNull() {
        String returnStr;
        if (str == null || "null".equals(str.toLowerCase())) {
            returnStr = "";
        } else {
            returnStr = str;
        }
        return returnStr;
    }

    public boolean isEmpty() {
        return (str == null || str.length() == 0);
    }

    public String encodeURI() {
        if (this.isEmpty()) {
            return "";
        }
        try {
            return java.net.URLEncoder.encode(str, "utf-8");
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public String decodeURI() {
        if (this.isEmpty()) {
            return "";
        }
        try {
            return java.net.URLDecoder.decode(str, "utf-8");
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public String edes(String key) throws Exception {
        return new DesUtils(str).encrypt(key);
    }

    public String ddes(String key) throws Exception {
        return new DesUtils(str).decrypt(key);
    }

    @Override
    public String toString() {
        return this.str;
    }

    public Date toDate() {
        return Jdate.with(str).getDate();
    }

    public int toInt() {
        return Integer.parseInt(str);
    }

    public Long toLong() {
        return Long.parseLong(str);
    }

    public double toDouble() {
        return Double.parseDouble(this.str);
    }

    public float toFloat() {
        return Float.parseFloat(str);
    }

    public boolean toBoolean() {
        return Boolean.parseBoolean(str);
    }

    public OutputStream toOutputStream() throws IOException {
        OutputStream out = new ByteArrayOutputStream();
        out.write(this.str.getBytes());
        return out;
    }

    public Jstring toFile(String filepath) throws Exception {
        Jile.with(filepath).write(this.str);
        return this;
    }

    public Jstring subString(int n) {
        try {
            int num = 0;
            byte[] buf = str.getBytes("GBK");
            if (n >= buf.length) {
                return this;
            }
            boolean bChineseFirstHalf = false;
            for (int i = 0; i < n; i++) {
                if (buf[i] < 0 && !bChineseFirstHalf) {
                    bChineseFirstHalf = true;
                } else {
                    num++;
                    bChineseFirstHalf = false;
                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return new Jstring(str);
    }

    public static void main(String[] args) throws Exception {
//        System.out.println(Jstring.with("a,b,c,d,e,f").split(",").each(new JstringEach() {
//            @Override
//            public boolean each(String k, int index) {
//                System.out.println(k);
//                System.out.println(index);
//                return false;
//            }
//        }).md5());
        final StringBuffer mm = new StringBuffer("--");
        Jstring.with("123|345").split("\\|").each(new JstringEach() {
            @Override
            public boolean each(String k, int index) {
                System.out.println(k);
                mm.append(k);
                return false;
            }
        }).toFile("E:\\bb.txt");
        System.out.println(mm);
    }
}
