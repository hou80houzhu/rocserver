package com.rocui.jpager.base.macro;

import com.rocui.jpager.JpageController;
import com.rocui.jpager.base.URLQueryHandler;
import com.rocui.util.base.reflect.ObjectSnooper;
import freemarker.core.Environment;
import freemarker.template.ObjectWrapper;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

public class Provider implements TemplateDirectiveModel {

    @Override
    public void execute(Environment e, Map map, TemplateModel[] tms, TemplateDirectiveBody tdb) throws TemplateException, IOException {
        HttpServletRequest request = (HttpServletRequest) e.getCustomAttribute("request");
        URLQueryHandler urlSetter = (URLQueryHandler) e.getCustomAttribute("urlSetter");

        String controllerName = map.get("provider").toString();
        String actionName = map.get("method").toString();
        String dataname = map.get("name").toString();

        HashMap<String, Object> ct = (HashMap<String, Object>) map;

        try {
            System.out.println("---------------"+controllerName);
            String className = JpageController.get().getClassName(controllerName);
            if (className != null && className.length() > 0) {
                Object x = Class.forName(className).newInstance();
                HashMap<String, Object> mapx = new HashMap<String, Object>();
                mapx.put("out", e.getOut());
                mapx.put("request", request);
                mapx.put("parameters", ct);
                mapx.put("urlSetter", urlSetter);

                ObjectSnooper obj = ObjectSnooper.snoop(x);
                obj.sett(mapx);
                obj.set(this._get(request));
                Object result = obj.trigger(actionName);
                obj.triggerr("close");

                HashMap<String, Object> xxtmap = new HashMap<String, Object>();
                Object xtt = obj.gett("local");
                if (xtt != null) {
                    xxtmap = (HashMap<String, Object>) xtt;
                }
                e.setVariable(dataname, ObjectWrapper.DEFAULT_WRAPPER.wrap(result));
                e.setVariable(dataname + "_local", ObjectWrapper.DEFAULT_WRAPPER.wrap(xxtmap));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if (tdb != null) {
            tdb.render(e.getOut());
        }
    }

    private HashMap<String, Object> _get(HttpServletRequest request) {
        Map xt = request.getParameterMap();
        HashMap<String, Object> hasmap = new HashMap<String, Object>();
        Iterator i = xt.keySet().iterator();
        while (i.hasNext()) {
            String key = i.next().toString();
            hasmap.put(key, request.getParameter(key));
        }
        return hasmap;
    }
}
