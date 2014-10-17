package com.rocui.mvc.view.page.base.macro;

import com.rocui.mvc.view.page.Jspx;
import com.rocui.mvc.view.page.Jspx.Jtempx;
import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

public class IncludePage implements TemplateDirectiveModel {

    @Override
    public void execute(Environment e, Map map, TemplateModel[] tms, TemplateDirectiveBody tdb) throws TemplateException, IOException {
        String path = map.get("name").toString();
        try {
            Jtempx page = Jspx.getContainer().getJspx(path);

            HttpServletRequest request = (HttpServletRequest) e.getCustomAttribute("request");
            page.addCustormAttribute("request", request);
            page.appendData("Request", this.getRequestArributes(request));
            page.appendData("request", request);
            page.appendData("Session", this.getSessionArributes(request));
            page.appendData("Parameters", this.getParameters(request));
            page.appendData("session", request.getSession());
            page.appendData("localPath", request.getContextPath());
            page.appendData("httpPath", request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/");

            page.out(e.getOut(), false);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if (tdb != null) {
            tdb.render(e.getOut());
        }
    }

    private HashMap<String, Object> getRequestArributes(HttpServletRequest request) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        if (request != null) {
            Enumeration x = request.getAttributeNames();
            while (x.hasMoreElements()) {
                String name = (String) x.nextElement();
                Object t = request.getAttribute(name);
                map.put(name, t);
            }
        }
        return map;
    }

    private HashMap<String, Object> getSessionArributes(HttpServletRequest request) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        if (request != null) {
            Enumeration x = request.getSession().getAttributeNames();
            while (x.hasMoreElements()) {
                String name = (String) x.nextElement();
                Object t = request.getSession().getAttribute(name);
                map.put(name, t);
            }
        }
        return map;
    }

    private HashMap<String, Object> getParameters(HttpServletRequest request) {
        HashMap<String, Object> hasmap = new HashMap<String, Object>();
        if (request != null) {
            Map xt = request.getParameterMap();
            Iterator i = xt.keySet().iterator();
            while (i.hasNext()) {
                String key = i.next().toString();
                hasmap.put(key, request.getParameter(key));
            }
        }
        return hasmap;
    }
}
