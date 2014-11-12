package com.rocui.util.http;

import com.rocui.util.file.Jile;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import org.apache.http.HttpEntity;
import org.apache.http.client.CookieStore;
import org.apache.http.cookie.CookieOrigin;
import org.apache.http.util.EntityUtils;

public abstract class SimpleResponse {

    protected int status;
    protected String charset = "utf8";
    protected HashMap<String, String> hreader;
    protected HttpEntity entity;
    protected Long contentLength;
    protected CookieStore cookieStore;
    protected CookieOrigin cookieOrigin;

    protected Object[] arguments;

    public SimpleResponse(Object... args) {
        this.arguments = args;
    }

    public String getContentString() {
        try {
            return EntityUtils.toString(entity, this.charset);
        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }
    }

    public InputStream getContent() throws IOException {
        return this.entity.getContent();
    }

    public Jile contentWriteTo(String path) throws Exception {
        Jile file = Jile.with(path);
        this.entity.writeTo(file.toOutputStream());
        return file;
    }

    public void contentWriteTo(OutputStream out) throws IOException {
        this.entity.writeTo(out);
    }

    public abstract void success();

    public abstract void error();
}
