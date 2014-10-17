package com.rocui.jpager.base;

import com.rocui.util.base.Jdate;
import java.io.Writer;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
//import net.ymate.platform.commons.util.UUIDUtils;

public class JpageProviderBase {

    protected JpageQuery query;

    protected Writer out;
    protected URLQueryHandler urlSetter;
    protected HttpServletRequest request;
    protected HashMap<String, Object> parameters = new HashMap<String, Object>();

    private HashMap<String, Object> local = new HashMap<String, Object>();

    public JpageProviderBase() {
        this.query = new JpageQuery();
    }

    public void setLocalVariable(String key, Object value) {
        this.local.put(key, value);
    }

    private void close() {
        this.query.close();
    }

    public Writer getOut() {
        return out;
    }

    public JpageQuery getQuery() {
        return query;
    }

    protected Long timeStamp() {
        return Jdate.now().timeStamp();
    }

    protected String dateTime() {
        return Jdate.now().dateTime();
    }

//    protected String uuid() {
//        return UUIDUtils.uuid();
//    }

    public URLQueryHandler getUrlSetter() {
        return urlSetter;
    }

    public HashMap<String, Object> getTagParameters() {
        return parameters;
    }

    public Object getTagParamter(String key) {
        return this.parameters.get(key);
    }

    public boolean hasTagParameter(String key) {
        return this.parameters.containsKey(key);
    }

    public String getLocalPath() {
        return this.request.getSession().getServletContext().getRealPath("/");
    }

    public String getHttpPath() {
        return this.request.getScheme() + "://" + this.request.getServerName() + ":" + this.request.getServerPort() + this.request.getContextPath() + "/";
    }

}
