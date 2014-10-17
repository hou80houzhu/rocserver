package com.rocui.jmail.page.macro;

import com.rocui.jmail.page.JmailPage;
import com.rocui.jmail.page.JmailProviderBase;
import com.rocui.util.base.reflect.ObjectSnooper;
import freemarker.core.Environment;
import freemarker.template.ObjectWrapper;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Provider implements TemplateDirectiveModel {

    @Override
    public void execute(Environment e, Map map, TemplateModel[] tms, TemplateDirectiveBody tdb) throws TemplateException, IOException {
        String controllerName = map.get("provider").toString();
        String actionName = map.get("method").toString();
        String dataname = map.get("name").toString();
        try {
            if (controllerName != null && controllerName.length() > 0) {
                HashMap<String, Object> info = new HashMap<String, Object>();
                String[] tp = e.getCustomAttributeNames();
                for (String tpp : tp) {
                    info.put(tpp, e.getCustomAttribute(tpp));
                }
                JmailProviderBase provider = JmailPage.getContainer().getProvider(controllerName);
                ObjectSnooper xt = ObjectSnooper.snoop(provider);
                HashMap<String, Object> mapx = new HashMap<String, Object>();
                mapx.put("out", e.getOut());
                mapx.put("parameters", (HashMap<String, Object>) map);
                xt.sett(mapx);
                xt.set(info);
                Object result = xt.trigger(actionName);
                xt.trigger("close");
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
}
