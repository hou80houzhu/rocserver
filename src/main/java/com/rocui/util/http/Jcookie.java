package com.rocui.util.http;

import java.util.Date;
import org.apache.http.impl.cookie.BasicClientCookie;

public class Jcookie {

    protected BasicClientCookie cookie;

    private Jcookie(String key, String value) {
        this.cookie = new BasicClientCookie(key, value);
    }

    public static Jcookie getCookie(String key, String value) {
        return new Jcookie(key, value);
    }

    public Jcookie setComment() {
        this.cookie.setComment(null);
        return this;
    }

    public Jcookie setAttribute(String key, String value) {
        this.cookie.setAttribute(key, value);
        return this;
    }

    public Jcookie setDomain(String domain) {
        this.cookie.setDomain(domain);
        return this;
    }

    public Jcookie setExpiryDate(Date date) {
        this.cookie.setExpiryDate(date);
        return this;
    }

    public Jcookie setSecure(boolean secure) {
        this.cookie.setSecure(secure);
        return this;
    }

    public Jcookie setValue(String value) {
        this.cookie.setValue(value);
        return this;
    }

    public Jcookie setVersion(int version) {
        this.cookie.setVersion(version);
        return this;
    }
}
