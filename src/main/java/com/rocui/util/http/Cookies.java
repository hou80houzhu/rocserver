package com.rocui.util.http;

import java.util.Date;
import org.apache.http.impl.cookie.BasicClientCookie;

public class Cookies {

    protected BasicClientCookie cookie;

    private Cookies(String key, String value) {
        this.cookie = new BasicClientCookie(key, value);
    }

    public static Cookies getCookie(String key, String value) {
        return new Cookies(key, value);
    }

    public Cookies setComment() {
        this.cookie.setComment(null);
        return this;
    }

    public Cookies setAttribute(String key, String value) {
        this.cookie.setAttribute(key, value);
        return this;
    }

    public Cookies setDomain(String domain) {
        this.cookie.setDomain(domain);
        return this;
    }

    public Cookies setExpiryDate(Date date) {
        this.cookie.setExpiryDate(date);
        return this;
    }

    public Cookies setSecure(boolean secure) {
        this.cookie.setSecure(secure);
        return this;
    }

    public Cookies setValue(String value) {
        this.cookie.setValue(value);
        return this;
    }

    public Cookies setVersion(int version) {
        this.cookie.setVersion(version);
        return this;
    }
}
