package com.rocui.jmail.page;

import com.rocui.util.base.Jdate;
import java.io.Writer;
import java.util.HashMap;
//import net.ymate.platform.commons.util.UUIDUtils;

public class JmailProviderBase {

    protected Writer out;
    private HashMap<String, Object> parameters;
    protected JmailQuery query;

    public JmailProviderBase() {
        this.query = new JmailQuery();
    }

    private void close() {
        this.query.close();
    }

    public Writer getOut() {
        return out;
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

    public HashMap<String, Object> getTagParameters() {
        return parameters;
    }

    public Object getTagParamter(String key) {
        return this.parameters.get(key);
    }

    public boolean hasTagParameter(String key) {
        return this.parameters.containsKey(key);
    }
}
