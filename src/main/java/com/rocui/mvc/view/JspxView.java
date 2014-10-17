package com.rocui.mvc.view;

import com.rocui.mvc.view.page.Jspx;
import com.rocui.mvc.view.page.Jspx.Jtempx;
import java.io.OutputStream;
import java.io.Writer;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class JspxView extends View {

    private String path;
    private HashMap<String, Object> map = new HashMap<String, Object>();

    public JspxView(String path) {
        this.path = path;
    }

    public JspxView setAttribute(String name, Object value) {
        this.map.put(name, value);
        return this;
    }

    public Map<String, Object> getAttributes() {
        return this.map;
    }

    public void out(OutputStream out) throws Exception {
        Jtempx jspx = Jspx.getContainer().getJspx(path);
        jspx.addCustormAttribute("request", request);
        jspx.appendData("Request", this.getRequestArributes());
        jspx.appendData("request", request);
        jspx.appendData("Session", this.getSessionArributes());
        jspx.appendData("Parameters", this.getParameters());
        jspx.appendData("session", request.getSession());
        jspx.appendData("localPath", request.getContextPath());
        jspx.appendData("httpPath", request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/");
        jspx.appendData("Page", this.map);
        String k = jspx.getString();
        out.write(k.getBytes());
    }

    public String getString() throws Exception {
        Jtempx jspx = Jspx.getContainer().getJspx(path);
        jspx.addCustormAttribute("request", request);
        jspx.appendData("Page", this.map);
        jspx.appendData("Request", this.getRequestArributes());
        jspx.appendData("Session", this.getSessionArributes());
        jspx.appendData("Parameters", this.getParameters());
        jspx.appendData("request", request);
        jspx.appendData("session", request.getSession());
        jspx.appendData("localPath", request.getContextPath());
        jspx.appendData("httpPath", request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/");
        return jspx.getString();
    }

    private HashMap<String, Object> getRequestArributes() {
        HashMap<String, Object> map = new HashMap<String, Object>();
        if (this.request != null) {
            Enumeration x = this.request.getAttributeNames();
            while (x.hasMoreElements()) {
                String name = (String) x.nextElement();
                Object t = this.request.getAttribute(name);
                map.put(name, t);
            }
        }
        return map;
    }

    private HashMap<String, Object> getSessionArributes() {
        HashMap<String, Object> map = new HashMap<String, Object>();
        if (this.request != null) {
            Enumeration x = this.request.getSession().getAttributeNames();
            while (x.hasMoreElements()) {
                String name = (String) x.nextElement();
                Object t = this.request.getSession().getAttribute(name);
                map.put(name, t);
            }
        }
        return map;
    }

    private HashMap<String, Object> getParameters() {
        HashMap<String, Object> hasmap = new HashMap<String, Object>();
        if (this.request != null) {
            Map xt = request.getParameterMap();
            Iterator i = xt.keySet().iterator();
            while (i.hasNext()) {
                String key = i.next().toString();
                hasmap.put(key, request.getParameter(key));
            }
        }
        return hasmap;
    }

    @Override
    public void out() throws Exception {
        this.request.setAttribute("message", "xxxxxxxxxxx");
        this.request.setAttribute("tag", "xxxxxxxxxxx");
        Writer writer = response.getWriter();
        writer.write(this.getString());
        writer.flush();
        writer.close();
    }
}
