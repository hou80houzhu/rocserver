package com.rocui.jmsger.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class JmsgerUtils {

    public static String getCurrentTime() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return df.format(new Date());
    }
}
