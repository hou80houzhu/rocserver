package com.rocui.jmail.page;

import com.rocui.mvc.view.page.Jspx;
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
import java.util.Map;
import java.util.Map.Entry;

public abstract class JmailTagBase implements TemplateDirectiveModel {

    protected Map<String, Object> tagParameter;
    protected Writer out;

    @Override
    public void execute(Environment e, Map map, TemplateModel[] tms, TemplateDirectiveBody tdb) throws TemplateException, IOException {
        this.tagParameter = map;
        this.out = e.getOut();
        String[] tp = e.getCustomAttributeNames();
        HashMap<String, Object> info = new HashMap<String, Object>();
        for (String tpp : tp) {
            info.put(tpp, e.getCustomAttribute(tpp));
        }
        ObjectSnooper.snoop(this).set(info);
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
        String className = Jspx.getContainer().getControllerName(providerName);
        ObjectSnooper snooper = ObjectSnooper.snoop(className);
        Object c = snooper.object();
        if (c instanceof JmailProviderBase) {
            HashMap<String, Object> mapx = new HashMap<String, Object>();
            mapx.put("out", out);
            mapx.put("parameters", tagParameter);
            Object result = null;
            try {
                result = snooper.trigger(methodName);
            } catch (Exception e) {
                e.printStackTrace();
            }
            snooper.trigger("close");
            return result;
        } else {
            throw new Exception("[JmailTag:Provider can not find.]");
        }
    }

    protected Object getTagParameter(String key) {
        return this.tagParameter.get(key);
    }
}
