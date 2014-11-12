package com.rocui.util.http;

import java.util.Date;
import org.apache.http.impl.cookie.BasicClientCookie;

public class SimpleCookies {

    protected BasicClientCookie cookie;

    private SimpleCookies(String key, String value) {
        this.cookie = new BasicClientCookie(key, value);
    }

    public static SimpleCookies getCookie(String key, String value) {
        return new SimpleCookies(key, value);
    }

    public SimpleCookies setComment() {
        this.cookie.setComment(null);
        return this;
    }

    public SimpleCookies setAttribute(String key, String value) {
        this.cookie.setAttribute(key, value);
        return this;
    }

    public SimpleCookies setDomain(String domain) {
        this.cookie.setDomain(domain);
        return this;
    }

    public SimpleCookies setExpiryDate(Date date) {
        this.cookie.setExpiryDate(date);
        return this;
    }

    public SimpleCookies setSecure(boolean secure) {
        this.cookie.setSecure(secure);
        return this;
    }

    public SimpleCookies setValue(String value) {
        this.cookie.setValue(value);
        return this;
    }

    public SimpleCookies setVersion(int version) {
        this.cookie.setVersion(version);
        return this;
    }
}
