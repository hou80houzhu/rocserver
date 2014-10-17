package com.rocui.mvc.view.page;

import com.rocui.jmail.page.macro.IncludePage;
import com.rocui.mvc.view.page.annotation.JspxMethod;
import com.rocui.mvc.view.page.annotation.JspxProvider;
import com.rocui.mvc.view.page.annotation.JspxTag;
import com.rocui.mvc.view.page.base.JspxException;
import com.rocui.mvc.view.page.base.macro.Provider;
import com.rocui.util.base.PackageBrowser;
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
import org.apache.log4j.Logger;

public class Jspx {

    private static Jspx pager = null;
    private String suffix = "html";
    private String encoding = "utf-8";
    private Configuration cfg = new Configuration();
    private HashMap<String, String> classmapping = new HashMap<>();

    private Jspx(Jsonx json) {
        try {
            String root = this.getClass().getClassLoader().getResource("/").getPath();
            File xmt = new File(root);
//            System.out.println("==============>>>"+xmt.getParentFile().getAbsolutePath());
            this.suffix = json.hasKey("suffix") ? json.get("suffix").toString() : "html";
            this.encoding = json.hasKey("encoding") ? json.get("encoding").toString() : "utf-8";
            FileTemplateLoader loader = new FileTemplateLoader(new File(xmt.getParentFile().getAbsolutePath() + json.get("jspxPath").toString()));
            cfg.setTemplateLoader(loader);
            cfg.setDefaultEncoding(json.get("encoding").toString());
            cfg.setTemplateExceptionHandler(new JspxException());
            cfg.setSharedVariable("page", new IncludePage());
            cfg.setSharedVariable("data", new Provider());
            List<String> service = PackageBrowser.getClassNames(json.get("tagPakage").toString());
            if (service != null && service.size() > 0) {
                for (String x : service) {
                    if (Class.forName(x).isAnnotationPresent(JspxTag.class)) {
                        String xname = Class.forName(x).getAnnotation(JspxTag.class).name();
                        cfg.setSharedVariable(xname, Class.forName(x).newInstance());
                        Logger.getLogger(Jspx.class).info("viewTag:"+ xname + ":" + x);
//                        System.out.println("[view tags] name:" + xname + ":" + x);
                    }
                }
            }
            List<String> method = PackageBrowser.getClassNames(json.get("methodPakage").toString());
            if (service != null && method.size() > 0) {
                for (String x : method) {
                    if (Class.forName(x).isAnnotationPresent(JspxMethod.class)) {
                        String xname = Class.forName(x).getAnnotation(JspxMethod.class).name();
                        cfg.setSharedVariable(xname, Class.forName(x).newInstance());
//                        System.out.println("[view method] name:" + xname + ":" + x);
                        Logger.getLogger(Jspx.class).info("viewMethod:"+ xname + ":" + x);
                    }
                }
            }
            List<String> controllers = PackageBrowser.getClassNames(json.get("providerPakage").toString());
            if (controllers != null && controllers.size() > 0) {
                for (String x : controllers) {
                    if (Class.forName(x).isAnnotationPresent(JspxProvider.class)) {
                        String xname = Class.forName(x).getAnnotation(JspxProvider.class).name();
//                        System.out.println("[view provider] name:" + xname + ":" + x);
                        Logger.getLogger(Jspx.class).info("viewProvider:"+ xname + ":" + x);
                        this.classmapping.put(xname, x);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void init(Jsonx json) {
        Jspx.pager = new Jspx(json);
    }

    public static Jspx getContainer() {
        return Jspx.pager;
    }

    public Jspx addDirect(String name, String classPath) throws Exception {
        cfg.setSharedVariable(name, Class.forName(classPath).newInstance());
        return this;
    }

    public Jtempx getJspx(String jspxName) throws IOException {
        return new Jtempx(jspxName + "." + this.suffix);
    }

    public String getControllerName(String key) {
        return this.classmapping.get(key);
    }

    public class Jtempx {

        private Template t = null;
        private HashMap<String, Object> evnobj = new HashMap<String, Object>();
        private HashMap<String, Object> data = new HashMap<String, Object>();

        private boolean isnull = false;

        public Jtempx(String tempName) throws IOException {
            try {
                t = cfg.getTemplate(tempName, encoding);
            } catch (IOException e) {
                isnull = true;
                if (e instanceof ParseException) {
                    t = cfg.getTemplate("500." + suffix, encoding);
                    this.appendData("message", e.getMessage());
                } else {
                    t = cfg.getTemplate("404." + suffix, encoding);
                    this.appendData("message", e.getMessage());
                };
                e.printStackTrace();
            }
        }

        public boolean isNull() {
            return this.isnull;
        }

        public Jtempx setData(HashMap<String, Object> value) {
            this.data = value;
            return this;
        }

        public Jtempx addCustormAttribute(String key, Object value) {
            this.evnobj.put(key, value);
            return this;
        }

        public Jtempx addCustormAttributes(HashMap<String, Object> map) {
            this.evnobj.putAll(map);
            return this;
        }

        public Jtempx removeCustormAttribute(String key) {
            this.evnobj.remove(key);
            return this;
        }

        public Jtempx appendData(String key, Object value) {
            this.data.put(key, value);
            return this;
        }

        public Jtempx appendData(HashMap<String, Object> data) {
            if (data != null) {
                this.data.putAll(data);
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
