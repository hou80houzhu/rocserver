package com.rocui.util.http;

import java.util.HashMap;
import org.apache.http.client.CookieStore;
import org.apache.http.cookie.CookieOrigin;

public abstract class Jresponse {

    protected int status;
    protected HashMap<String, String> hreader;
    protected String body;
    protected Long contentLength;
    protected CookieStore cookieStore;
    protected CookieOrigin cookieOrigin;

    public abstract void success(String result);

    public abstract void error();
}
