package com.rocui.CodeHelper;

import com.rocui.util.file.Jile;
import com.rocui.util.jsonx.Jsonx;
import freemarker.cache.FileTemplateLoader;
import freemarker.core.Environment;
import freemarker.template.Configuration;
import freemarker.template.Template;
import java.io.BufferedWriter;
import java.io.StringWriter;
import java.util.HashMap;

public abstract class BaseHelper {

    public String parseTemplte(String templatePath, HashMap<String, Object> vars) throws Exception {
        if (Jile.with(templatePath).file().isFile()) {
            Configuration cfg = new Configuration();
            FileTemplateLoader loader = new FileTemplateLoader(Jile.with(templatePath).file().getParentFile());
            cfg.setTemplateLoader(loader);
            cfg.setDefaultEncoding("utf8");
            Template t = cfg.getTemplate(Jile.with(templatePath).file().getName(), "utf8");
            StringWriter stringWriter = new StringWriter();
            BufferedWriter writer = new BufferedWriter(stringWriter);
            try {
                Environment evn = t.createProcessingEnvironment(vars, writer);
                evn.process();
                writer.flush();
                writer.close();
                return stringWriter.toString();
            } catch (Exception ex) {
                ex.printStackTrace();
                return null;
            }
        }
        return null;
    }

    public abstract void excute(Jsonx helpOption, Jsonx option);
}
