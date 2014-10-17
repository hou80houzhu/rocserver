package com.rocui.jmail.page;

import com.rocui.jmail.page.annotation.JmailMethod;
import com.rocui.jmail.page.annotation.JmailProvider;
import com.rocui.jmail.page.annotation.JmailTag;
import com.rocui.jmail.page.macro.IncludePage;
import com.rocui.jmail.page.macro.Provider;
import com.rocui.util.base.PackageBrowser;
import com.rocui.util.base.reflect.ObjectSnooper;
import com.rocui.util.jsonx.Jsonx;
import freemarker.cache.FileTemplateLoader;
import freemarker.core.Environment;
import freemarker.core.ParseException;
import freemarker.template.Configuration;
import freemarker.template.Template;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JmailPage {

    private static JmailPage pager = null;
    private String suffix = "html";
    private String encoding = "utf-8";
    private Configuration cfg = new Configuration();
    private HashMap<String, String> classmapping = new HashMap<String, String>();

    private JmailPage(Jsonx json) throws Exception {
        this.suffix = json.hasKey("suffix") ? json.get("suffix").toString() : "html";
        this.encoding = json.hasKey("encoding") ? json.get("encoding").toString() : "utf-8";
        FileTemplateLoader loader = new FileTemplateLoader(new File(System.getProperty("user.dir") + json.get("viewPath").toString()));
        cfg.setTemplateLoader(loader);
        cfg.setDefaultEncoding(json.get("encoding").toString());
        cfg.setTemplateExceptionHandler(new JmailException());
        cfg.setSharedVariable("page", new IncludePage());
        cfg.setSharedVariable("data", new Provider());
        List<String> service = PackageBrowser.getClassNames(json.get("tagPakage").toString());
        if (service != null && service.size() > 0) {
            for (String x : service) {
                if (Class.forName(x).isAnnotationPresent(JmailTag.class)) {
                    String xname = Class.forName(x).getAnnotation(JmailTag.class).name();
                    cfg.setSharedVariable(xname, Class.forName(x).newInstance());
                }
            }
        }
        List<String> method = PackageBrowser.getClassNames(json.get("methodPakage").toString());
        if (service != null && method.size() > 0) {
            for (String x : method) {
                if (Class.forName(x).isAnnotationPresent(JmailMethod.class)) {
                    String xname = Class.forName(x).getAnnotation(JmailMethod.class).name();
                    cfg.setSharedVariable(xname, Class.forName(x).newInstance());
                }
            }
        }
        List<String> controllers = PackageBrowser.getClassNames(json.get("providerPakage").toString());
        if (controllers != null && controllers.size() > 0) {
            for (String x : controllers) {
                if (Class.forName(x).isAnnotationPresent(JmailProvider.class)) {
                    String xname = Class.forName(x).getAnnotation(JmailProvider.class).name();
                    this.classmapping.put(xname, x);
                }
            }
        }
    }

    public static void init(Jsonx json) throws Exception {
        JmailPage.pager = new JmailPage(json);
    }

    public static JmailPage getContainer() {
        return JmailPage.pager;
    }

    public JmailPage addDirect(String name, String classPath) throws Exception {
        cfg.setSharedVariable(name, Class.forName(classPath).newInstance());
        return this;
    }

    public JmailTemp getMailTemp(String jspxName) throws IOException {
        return new JmailTemp(jspxName + "." + this.suffix);
    }

    public JmailProviderBase getProvider(String key) throws Exception {
        if (this.classmapping.containsKey(key)) {
            String name = this.classmapping.get(key);
            Object t = ObjectSnooper.snoop(name).object();
            if (t instanceof JmailProviderBase) {
                return (JmailProviderBase) t;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public class JmailTemp {

        private Template t = null;
        private HashMap<String, Object> evnobj = new HashMap<String, Object>();
        private HashMap<String, Object> data = new HashMap<String, Object>();

        private boolean isnull = false;

        public JmailTemp(String tempName) throws IOException {
            try {
                t = cfg.getTemplate(tempName, encoding);
            } catch (IOException e) {
                isnull = true;
                if (e instanceof ParseException) {
                    t = cfg.getTemplate("500." + suffix, encoding);
                    this.appendInfo("message", e.getMessage());
                } else {
                    t = cfg.getTemplate("404." + suffix, encoding);
                    this.appendInfo("message", e.getMessage());
                };
                e.printStackTrace();
            }
        }

        public boolean isNull() {
            return this.isnull;
        }

        public final JmailTemp appendInfo(String key, Object value) {
            this.evnobj.put(key, value);
            this.data.put(key, value);
            return this;
        }

        public JmailTemp appendInfos(HashMap<String, Object> data) {
            if (data != null) {
                this.data.putAll(data);
            }
            if (data != null) {
                this.evnobj.putAll(data);
            }
            return this;
        }

        public Template getTemplate() {
            return this.t;
        }

        public void print() throws Exception {
            Writer r = new PrintWriter(System.out);
            Environment evn = t.createProcessingEnvironment(this.data, r);
            for (Map.Entry<String, Object> x : this.evnobj.entrySet()) {
                evn.setCustomAttribute(x.getKey(), x.getValue());
            }
            evn.process();
        }

        public String getString() {
            StringWriter stringWriter = new StringWriter();
            BufferedWriter writer = new BufferedWriter(stringWriter);
            try {
                Environment evn = t.createProcessingEnvironment(data, writer);
                for (Map.Entry<String, Object> x : this.evnobj.entrySet()) {
                    evn.setCustomAttribute(x.getKey(), x.getValue());
                }
                evn.process();
                writer.flush();
                writer.close();
                return stringWriter.toString();
            } catch (Exception ex) {
                ex.printStackTrace();
                return null;
            }
        }

        public void out(Writer writer) throws Exception {
            Environment evn = t.createProcessingEnvironment(data, writer);
            for (Map.Entry<String, Object> x : this.evnobj.entrySet()) {
                evn.setCustomAttribute(x.getKey(), x.getValue());
            }
            evn.process();
            writer.flush();
            writer.close();
        }

        public void out(Writer writer, boolean isclose) throws Exception {
            Environment evn = t.createProcessingEnvironment(data, writer);
            for (Map.Entry<String, Object> x : this.evnobj.entrySet()) {
                evn.setCustomAttribute(x.getKey(), x.getValue());
            }
            evn.process();
            if (isclose) {
                writer.flush();
                writer.close();
            }
        }
    }
}
