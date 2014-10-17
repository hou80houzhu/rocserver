package com.rocui.jpager.base.macro;

import com.rocui.jpager.JmoduleContainer;
import com.rocui.jpager.JmoduleContainer.JpageModule;
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

public class Module implements TemplateDirectiveModel {

    @Override
    public void execute(Environment e, Map map, TemplateModel[] tms, TemplateDirectiveBody tdb) throws TemplateException, IOException {
        String modulename = map.get("name").toString();
        String moduleid = map.get("id").toString();
        try {
            JpageModule module = JmoduleContainer.getContainer().getActiveModule(moduleid, modulename);
            if (module != null) {
                HttpServletRequest request = (HttpServletRequest) e.getCustomAttribute("request");
                module.setRequest(request);

                module.addVariable("session", request.getSession());
                module.addVariable("request", request);
                module.addVariable("Request", this.getRequestArributes(request));
                module.addVariable("Session", this.getSessionArributes(request));
                module.addVariable("Parameter", this.getParameters(request));
                module.addVariable("BasePath", e.getCustomAttribute("BasePath"));
                module.addVariable("HttpPath", e.getCustomAttribute("HttpPath"));

                e.getOut().write(module.getModuleContent());
            }
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
