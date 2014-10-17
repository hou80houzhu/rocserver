package com.rocui.mvc.view.page.base.macro;

import com.rocui.mvc.view.page.Jspx;
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

/**
 *
 * @author Jinliang
 */
public class Provider implements TemplateDirectiveModel {

    @Override
    public void execute(Environment e, Map map, TemplateModel[] tms, TemplateDirectiveBody tdb) throws TemplateException, IOException {
        HttpServletRequest request = (HttpServletRequest) e.getCustomAttribute("request");
        String controllerName = map.get("provider").toString();
        String actionName = map.get("method").toString();
        String dataname = map.get("name").toString();
        try {
            String className = Jspx.getContainer().getControllerName(controllerName);
            if (className != null && className.length() > 0) {
                ObjectSnooper xt = ObjectSnooper.snoop(className);
                HashMap<String, Object> mapx = new HashMap<String, Object>();
                mapx.put("request", request);
                mapx.put("out", e.getOut());
                mapx.put("parameters", (HashMap<String,Object>)map);
                xt.sett(mapx);
                xt.set(this._get(request));
                Object result = xt.trigger(actionName);
                if (result != null) {
                    e.setVariable(dataname, ObjectWrapper.DEFAULT_WRAPPER.wrap(result));
                }
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
