package com.rocui.jmail.page.macro;

import com.rocui.jmail.page.JmailPage;
import com.rocui.jmail.page.JmailPage.JmailTemp;
import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import java.io.IOException;
import java.util.Map;

public class IncludePage implements TemplateDirectiveModel {

    @Override
    public void execute(Environment e, Map map, TemplateModel[] tms, TemplateDirectiveBody tdb) throws TemplateException, IOException {
        String path = map.get("name").toString();
        try {
            JmailTemp page = JmailPage.getContainer().getMailTemp(path);
            String[] tp = e.getCustomAttributeNames();
            for (String tpp : tp) {
                page.appendInfo(tpp, e.getCustomAttribute(tpp));
            }
            page.out(e.getOut(), false);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if (tdb != null) {
            tdb.render(e.getOut());
        }
    }

}
