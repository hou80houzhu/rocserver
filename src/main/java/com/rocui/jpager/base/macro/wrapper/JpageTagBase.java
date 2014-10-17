package com.rocui.jpager.base.macro.wrapper;

import com.rocui.jpager.JpageController;
import com.rocui.jpager.base.JpageProviderBase;
import com.rocui.jpager.base.URLQueryHandler;
import com.rocui.util.base.reflect.ObjectSnooper;
import freemarker.core.Environment;
import freemarker.template.ObjectWrapper;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import javax.servlet.http.HttpServletRequest;

public abstract class JpageTagBase implements TemplateDirectiveModel {

    protected Map<String, Object> tagParameter;
    protected Writer out;
    protected HttpServletRequest request;
    protected URLQueryHandler urlSetter;

    @Override
    public void execute(Environment e, Map map, TemplateModel[] tms, TemplateDirectiveBody tdb) throws TemplateException, IOException {
        tagParameter = map;
        request = (HttpServletRequest) e.getCustomAttribute("request");
        urlSetter = (URLQueryHandler) e.getCustomAttribute("urlSetter");
        out = e.getOut();

        HashMap<String, Object> mapx = this.doService();
        if (mapx != null) {
            for (Entry<String, Object> c : mapx.entrySet()) {
                e.setVariable(c.getKey(), ObjectWrapper.DEFAULT_WRAPPER.wrap(c.getValue()));
            }
        }

        if (tdb != null) {
            tdb.render(e.getOut());
        }
    }

    public abstract HashMap<String, Object> doService();

    public Object getProviderData(String providerName, String methodName) throws Exception {
        String className =  JpageController.get().getClassName(providerName);
        System.out.println("===========>"+providerName);
        ObjectSnooper snooper = ObjectSnooper.snoop(className);
        Object c = snooper.object();
        if (c instanceof JpageProviderBase) {
            HashMap<String, Object> mapx = new HashMap<String, Object>();
            mapx.put("out", out);
            mapx.put("request", request);
            mapx.put("parameters", tagParameter);
            mapx.put("urlSetter", urlSetter);
            snooper.sett(mapx).set(this._get(request));
            Object result = null;
            try {
                result = snooper.trigger(methodName);
            } catch (Exception e) {
                e.printStackTrace();
            }
            snooper.trigger("close");
            return result;
        } else {
            throw new Exception("[JpageTag:Provider can not find.]");
        }
    }

    protected Object getTagParameter(String key) {
        return this.tagParameter.get(key);
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
