package com.rocui.mvc.view.page.base;

import com.rocui.util.base.Jdate;
import java.io.Writer;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
//import net.ymate.platform.commons.util.UUIDUtils;

public class JspxProviderBase {

    protected HttpServletRequest request;
    protected Writer out;
    private HashMap<String,Object> parameters;

    public HttpServletRequest getRequest() {
        return request;
    }

    public Writer getOut() {
        return out;
    }

    protected String localPath() {
        return this.request.getSession().getServletContext().getRealPath("/");
    }

    protected String httpLocalPath() {
        return this.request.getScheme() + "://" + this.request.getServerName() + ":" + this.request.getServerPort() + this.request.getContextPath() + "/";
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

    public HashMap<String,Object> getTagParameters() {
        return parameters;
    }

    public Object getTagParamter(String key) {
        return this.parameters.get(key);
    }

    public boolean hasTagParameter(String key) {
        return this.parameters.containsKey(key);
    }
    
}
