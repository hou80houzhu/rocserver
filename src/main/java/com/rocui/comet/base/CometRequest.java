package com.rocui.comet.base;

import com.rocui.util.jsonx.Jsonx;
import javax.servlet.http.HttpServletRequest;

public class CometRequest {

    private String id;
    private String type;
    private String parameter;
    
    private Jsonx pjson;
    private CometSession session;
    private HttpServletRequest request;

    public String getId() {
        return id;
    }

    protected void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    protected void setType(String type) {
        this.type = type;
    }

    public Object getParameter(String key) throws Exception {
        if (this.pjson != null) {
            return this.pjson.get(key).data();
        } else {
            return null;
        }
    }

    protected void setParameter(String parameter) {
        this.parameter = parameter;
        try {
            this.pjson = Jsonx.create(parameter);
        } catch (Exception ex) {
            this.pjson = null;
        }
    }

    public CometSession getSession() {
        return session;
    }

    protected void setSession(CometSession session) {
        this.session = session;
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    protected void setRequest(HttpServletRequest request) {
        this.request = request;
    }

}
