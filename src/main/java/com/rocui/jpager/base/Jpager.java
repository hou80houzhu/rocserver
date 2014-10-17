package com.rocui.jpager.base;

import com.rocui.jpager.JmoduleContainer;
import com.rocui.jpager.JpageContainer;
import com.rocui.jpager.JpageController;
import com.rocui.jpager.annotation.PageMethod;
import com.rocui.jpager.annotation.PageTag;
import com.rocui.jpager.base.macro.Provider;
import com.rocui.jpager.base.macro.Module;
import com.rocui.util.base.PackageBrowser;
import com.rocui.util.jsonx.JsonEachArray;
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
import java.util.Map.Entry;

public class Jpager {

    private static Jpager pager = null;
    private Configuration cfg = new Configuration();
    private String suffix = "html";
    private String cachePath = "";
    private String encoding = "utf-8";
    private HashMap<String, Long> cachelist = new HashMap<String, Long>();

    private Jsonx config;

    private Jpager(Jsonx json) throws Exception {
        this.config = json;
        this.suffix = json.get("suffix").toString();
        this.cachePath = System.getProperty("user.dir") + json.get("cachePath").toString();
        if (json.hasKey("cacheList")) {
            json.get("cacheList").each(new JsonEachArray(this.cachelist) {
                @Override
                public boolean each(int index, Jsonx json) {
                    HashMap<String, Long> map = (HashMap<String, Long>) this.arguments[0];
                    try {
                        map.put(json.get("pageName").toString(), json.get("time").toLong());
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    return false;
                }
            });
        }
        FileTemplateLoader loader = new FileTemplateLoader(new File(System.getProperty("user.dir") + json.get("viewPath").toString()));
        cfg.setTemplateLoader(loader);
        cfg.setDefaultEncoding(json.get("encoding").toString());
        cfg.setTemplateExceptionHandler(new JpageException());
        cfg.setSharedVariable("module", new Module());
        cfg.setSharedVariable("data", new Provider());
        List<String> service = PackageBrowser.getClassNames(json.get("tagPakage").toString());
        if (service != null && service.size() > 0) {
            for (String x : service) {
                if (Class.forName(x).isAnnotationPresent(PageTag.class)) {
                    String xname = Class.forName(x).getAnnotation(PageTag.class).name();
                    cfg.setSharedVariable(xname, Class.forName(x).newInstance());
                }
            }
        }
        List<String> method = PackageBrowser.getClassNames(json.get("methodPakage").toString());
        if (service != null && method.size() > 0) {
            for (String x : method) {
                if (Class.forName(x).isAnnotationPresent(PageMethod.class)) {
                    String xname = Class.forName(x).getAnnotation(PageMethod.class).name();
                    cfg.setSharedVariable(xname, Class.forName(x).newInstance());
                }
            }
        }
    }

    public static void init(Jsonx json) throws Exception {
        if (Jpager.pager == null) {
            Jpager.pager = new Jpager(json);
            JpageController.init(Jpager.pager.config.get("providerPakage").toString());
            JpageContainer.init(Jpager.pager.config.get("viewPath").toString());
            JmoduleContainer.init(Jpager.pager.config.get("viewPath").toString() + File.separator + "modules");
        }
    }

    public static Jpager getPager() {
        return Jpager.pager;
    }

    public Jpager addDirective(String name, String classPath) throws Exception {
        cfg.setSharedVariable(name, Class.forName(classPath).newInstance());
        return this;
    }

    public Jtemp getTemplate(String tempName) throws Exception {
        return new Jtemp(tempName + "." + this.suffix);
    }

    public String getCachePath() {
        return this.cachePath;
    }

    public HashMap<String, Long> getCacheList() {
        return this.cachelist;
    }

    public Jsonx getConfig() {
        return this.config;
    }

    public class Jtemp {

        private Template t = null;
        private HashMap<String, Object> evnobj = new HashMap<String, Object>();
        private HashMap<String, Object> data = new HashMap<String, Object>();

        private boolean isnull = false;

        public Jtemp(String tempName) throws Exception {
            try {
                t = cfg.getTemplate(tempName);
            } catch (IOException e) {
                isnull = true;
                if (e instanceof ParseException) {
                    t = cfg.getTemplate("500." + suffix, encoding);
                    HashMap<String, Object> kt = new HashMap<String, Object>();
                    kt.put("message", e.getMessage());
                    kt.put("stackTrace", e.getStackTrace().toString());
                    this.append("Error", kt);
                    System.out.println("[jpager] the template with name " + tempName + " parsed error." + e.getMessage());
                } else {
                    t = cfg.getTemplate("404." + suffix, encoding);
                    HashMap<String, Object> kt = new HashMap<String, Object>();
                    kt.put("message", e.getMessage());
                    kt.put("stackTrace", e.getStackTrace().toString());
                    this.append("Error", kt);
                    System.out.println("[jpager] the template with name " + tempName + " can not find.");
                };
            }
        }

        public Jtemp setData(HashMap<String, Object> value) {
            this.data = value;
            return this;
        }

        public Jtemp addSessionAttribute(String key, Object value) {
            this.evnobj.put(key, value);
            return this;
        }

        public Jtemp removeSessionAttribute(String key) {
            this.evnobj.remove(key);
            return this;
        }

        public Jtemp append(String key, Object value) {
            this.data.put(key, value);
            return this;
        }

        public Jtemp append(HashMap<String, Object> data) {
            this.data.putAll(data);
            return this;
        }

        public Template getTemplate() {
            return this.t;
        }

        public void print() throws Exception {
            Writer r = new PrintWriter(System.out);
            Environment evn = t.createProcessingEnvironment(this.data, r);
            for (Entry<String, Object> x : this.evnobj.entrySet()) {
                evn.setCustomAttribute(x.getKey(), x.getValue());
            }
            evn.process();
        }

        public String getContent() {
            StringWriter stringWriter = new StringWriter();
            BufferedWriter writer = new BufferedWriter(stringWriter);
            try {
                Environment evn = t.createProcessingEnvironment(data, writer);
                for (Entry<String, Object> x : this.evnobj.entrySet()) {
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
            for (Entry<String, Object> x : this.evnobj.entrySet()) {
                evn.setCustomAttribute(x.getKey(), x.getValue());
            }
            evn.process();
            writer.flush();
            writer.close();
        }

        public void out(Writer writer, boolean isclose) throws Exception {
            Environment evn = t.createProcessingEnvironment(data, writer);
            for (Entry<String, Object> x : this.evnobj.entrySet()) {
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
