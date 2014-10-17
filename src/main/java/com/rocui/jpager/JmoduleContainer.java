package com.rocui.jpager;

import com.rocui.jpager.base.Jpager;
import com.rocui.jpager.base.Jpager.Jtemp;
import com.rocui.jpager.base.URLQueryHandler;
import com.rocui.util.jsonx.JsonEach;
import com.rocui.util.jsonx.JsonEachArray;
import com.rocui.util.jsonx.Jsonx;
import java.io.File;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class JmoduleContainer {

    private static JmoduleContainer cache = null;
    private String modulePath = "";
    private HashMap<String, HashMap<String, Object>> map = new HashMap<String, HashMap<String, Object>>();
    private final HashMap<String, JpageModule> defaultMap = new HashMap<String, JpageModule>();
    private HashMap<String, List<HashMap<String, Object>>> sortMap;

    private JmoduleContainer(String path) {
        this.modulePath = System.getProperty("user.dir") + path;
        this.setAllModules();
        this.setAllBuildModules();
        this.setSortMap();
    }

    public synchronized static void init(String path) {
        if (JmoduleContainer.cache == null) {
            JmoduleContainer.cache = new JmoduleContainer(path);
        }
    }

    public static JmoduleContainer getContainer() {
        return JmoduleContainer.cache;
    }

    private void setAllBuildModules() {
        File modulsoption = new File(this.modulePath + File.separator + "build.json");
        if (modulsoption.exists()) {
            try {
                Jsonx x = Jsonx.create(modulsoption);
                x.each(new JsonEach(this) {
                    @Override
                    public boolean each(String key, Jsonx json) {
                        try {
                            JmoduleContainer ths = (JmoduleContainer) this.arguments[0];
                            ths.map.put(key, json.toHashMap());
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        return false;
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void setAllModules() {
        File dir = new File(this.modulePath);
        File file[] = dir.listFiles();
        for (File file1 : file) {
            if (file1.isDirectory()) {
                String moduleName = file1.getName();
                String moduleOptionPath = modulePath + File.separator + moduleName + File.separator + moduleName + ".json";
                File optionFile = new File(moduleOptionPath);
                if (optionFile.exists()) {
                    try {
                        Jsonx json = Jsonx.create(new File(moduleOptionPath));
                        JpageModule module = new JpageModule(json.get("name").toString());
                        module.desc = json.get("desc").toString();
                        module.img = json.get("img").toString();
                        module.name = json.get("name").toString();
                        module.title = json.get("title").toString();
                        module.type = json.get("type").toString();
                        module.version = json.get("version").toString();
                        module.option = json.get("option").toString();
                        module.single = json.hasKey("single") ? json.get("single").toBoolean() : false;
                        module.editable = json.hasKey("editable") ? json.get("editable").toBoolean() : true;
                        module.fixed = json.hasKey("fixed") ? json.get("fixed").toBoolean() : false;
                        final HashMap<String, Object> map = new HashMap<String, Object>();
                        json.get("option").each(new JsonEachArray() {
                            @Override
                            public boolean each(int index, Jsonx json) {
                                try {
                                    map.put(json.get("name").toString(), json.get("value"));
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                                return false;
                            }
                        });
                        module.optionMapping = map;
                        this.defaultMap.put(moduleName, module);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }
    }

    private void setSortMap() {
        HashMap<String, List<HashMap<String, Object>>> map = new HashMap<String, List<HashMap<String, Object>>>();
        for (Entry<String, JpageModule> x : this.defaultMap.entrySet()) {
            List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
            String type = x.getValue().type;
            if (type == null || type.trim().length() == 0) {
                type = "Common";
                x.getValue().type = type;
            }
            if (!type.equals("System")) {
                map.put(type, list);
            }
        }
        for (Entry<String, JpageModule> x : this.defaultMap.entrySet()) {
            String type = x.getValue().type;
            if (map.containsKey(type)) {
                if (!x.getValue().fixed) {
                    map.get(type).add(x.getValue().getInfo());
                }
            }
        }
        HashMap<String, List<HashMap<String, Object>>> mapx = new HashMap<String, List<HashMap<String, Object>>>();
        for (Entry<String, List<HashMap<String, Object>>> x : map.entrySet()) {
            if (!x.getValue().isEmpty()) {
                mapx.put(x.getKey(), x.getValue());
            }
        }
        this.sortMap = mapx;
    }

    protected HashMap<String, HashMap<String, Object>> getActiveValuesMap() {
        return this.map;
    }

    protected void setActiveValuesMap(HashMap<String, HashMap<String, Object>> map) {
        this.map = map;
    }

    public JpageModule getDefaultModule(String moduleName) throws Exception {
        if (!this.defaultMap.containsKey(moduleName)) {
            JpageModule module = this.defaultMap.get("404");
            module.addVariable("moduleName", moduleName);
            return module;
        } else {
            return this.defaultMap.get(moduleName).reset();
        }
    }

    public JpageModule getActiveModule(String moduleId, String moduleName) throws Exception {
        JpageModule module = this.getDefaultModule(moduleName).cloneModule();
        HashMap<String, Object> x = this.getOptionValue(moduleId, moduleName);
        module.id = moduleId;
        module.optionMapping = x;
        return module;
    }

    public String getModuleOption(String moduleId, String moduleName) {
        HashMap<String, Object> values = this.getOptionValue(moduleId, moduleName);
        try {
            if (this.defaultMap.containsKey(moduleName)) {
                JpageModule module = this.defaultMap.get(moduleName).cloneModule();
                module.optionMapping = values;
                return module.getOptionString();
            } else {
                return null;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public HashMap<String, Object> getOptionValue(String moduleId, String moduleName) {
        if (this.map.containsKey(moduleId)) {
            return this.map.get(moduleId);
        } else {
            HashMap<String, Object> x = this.defaultMap.get(moduleName).optionMapping;
            HashMap<String, Object> xt = new HashMap<String, Object>();
            for (Entry<String, Object> n : x.entrySet()) {
                xt.put(n.getKey(), n.getValue());
            }
            this.map.put(moduleId, xt);
            return xt;
        }
    }

    public void setOptionValue(String moduleId, HashMap<String, Object> values) {
        if (this.map.containsKey(moduleId)) {
            HashMap<String, Object> c = this.map.get(moduleId);
            for (Entry<String, Object> d : values.entrySet()) {
                c.put(d.getKey(), d.getValue());
            }
        }
    }

    public List<HashMap<String, Object>> getModuleList() {
        List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
        for (Entry<String, JpageModule> x : this.defaultMap.entrySet()) {
            list.add(x.getValue().getInfo());
        }
        return list;
    }

    public HashMap<String, List<HashMap<String, Object>>> getModuleSortList() {
        return this.sortMap;
    }

    public void save() throws Exception {
        HashMap<String, Object> map = new HashMap<String, Object>();
        if (this.map.size() > 0) {
            for (Entry<String, HashMap<String, Object>> x : this.map.entrySet()) {
                if (x.getValue() != null) {
                    HashMap<String, Object> mapx = new HashMap<String, Object>();
                    for (Entry<String, Object> n : x.getValue().entrySet()) {
                        mapx.put(n.getKey(), n.getValue().toString());
                    }
                    map.put(x.getKey(), mapx);
                }
            }
        }
        Jsonx.create(map).toFile(this.modulePath + File.separator + "build.json");
    }

    public class JpageModule {

        private String id;
        private String name;
        private String title;
        private String img;
        private String desc;
        private String version = "1.0";
        private String type;
        private String option;
        private boolean single = false;
        private String pageName;
        private boolean editable;
        private boolean fixed;
        private Jtemp pager;
        private URLQueryHandler urlSetter;
        private HashMap<String, Object> optionMapping = new HashMap<String, Object>();

        public JpageModule(String moduleName) throws Exception {
            this.name = moduleName;
            this.pager = Jpager.getPager().getTemplate("modules" + File.separator + moduleName + File.separator + moduleName);
        }

        public JpageModule reset() throws Exception {
            this.pager = Jpager.getPager().getTemplate("modules" + File.separator + this.name + File.separator + this.name);
            return this;
        }

        public JpageModule setRequest(HttpServletRequest request) {
            pager.addSessionAttribute("request", request);
            pager.addSessionAttribute("urlSetter", new URLQueryHandler(request));
            return this;
        }

        public JpageModule addAttribute(String key, Object value) {
            pager.addSessionAttribute(key, value);
            return this;
        }

        public JpageModule addVariable(String key, Object value) {
            pager.append(key, value);
            return this;
        }

        public JpageModule addVariables(HashMap<String, Object> map) {
            pager.append(map);
            return this;
        }

        public String getOptionString() {
            try {
                return Jsonx.create(option).each(new JsonEachArray(this.optionMapping) {
                    @Override
                    public boolean each(int index, Jsonx json) {
                        HashMap<String, Object> xt = (HashMap<String, Object>) this.arguments[0];
                        try {
                            String key = json.get("name").toString();
                            json.set("value", xt.get(key));

                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        return false;
                    }
                }).toString();
            } catch (Exception ex) {
                ex.printStackTrace();
                return null;
            }
        }

        public HashMap<String, Object> getInfo() {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("desc", desc);
            map.put("id", id);
            map.put("img", img);
            map.put("name", name);
            map.put("title", title);
            map.put("type", type);
            map.put("version", version);
            map.put("single", single);
            map.put("editable", editable);
            map.put("fixed", fixed);
            return map;
        }

        private HashMap<String, Object> toVaules() {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("desc", desc);
            map.put("id", id);
            map.put("img", img);
            map.put("name", name);
            map.put("title", title);
            map.put("type", type);
            map.put("version", version);
            map.put("single", single);
            map.put("editable", editable);
            map.put("fixed", fixed);
            map.put("option", optionMapping);
            return map;
        }

        public String getModuleContent() {
            String moduleiId;
            if (this.isSingle()) {
                moduleiId = this.name + "-single";
            } else {
                if (this.id == null) {
                    moduleiId = System.currentTimeMillis() + "";
                } else {
                    moduleiId = this.id;
                }
            }
            pager.append("Module", this.toVaules());
            StringBuilder sb = new StringBuilder();
            sb.append("<div class='module' ");
            sb.append("module='");
            sb.append(this.name);
            sb.append("' id='");
            sb.append(moduleiId);
            sb.append("' editable='");
            sb.append(editable);
            if (fixed == true) {
                sb.append("' removeable='");
                sb.append(false);
            }
            sb.append("'>");
            sb.append(pager.getContent());
            sb.append("</div>");
            return sb.toString();
        }

        public JpageModule cloneModule() throws Exception {
            if (!this.isSingle()) {
                JpageModule module = new JpageModule(this.name);
                module.desc = desc;
                module.img = img;
                module.option = option;
                module.pageName = pageName;
                module.title = title;
                module.type = type;
                module.version = version;
                module.single = single;
                module.editable = editable;
                module.fixed = fixed;
                HashMap<String, Object> x = new HashMap<String, Object>();
                for (Entry<String, Object> n : optionMapping.entrySet()) {
                    x.put(n.getKey(), n.getValue());
                }
                module.optionMapping = x;
                return module;
            } else {
                return this;
            }
        }

        public void out(Writer writer) throws Exception {
            pager.append("Module", this.toVaules());
            pager.out(writer);
        }

        public void out(HttpServletResponse response) throws Exception {
            response.setContentType("text/html;charset=GBK");
            pager.append("Module", this.toVaules());
            pager.out(response.getWriter());
        }

        public String getName() {
            return name;
        }

        public String getTitle() {
            return title;
        }

        public String getImg() {
            return img;
        }

        public String getDesc() {
            return desc;
        }

        public String getOption() {
            return option;
        }

        public String getVersion() {
            return version;
        }

        public String getPageName() {
            return pageName;
        }

        public String getType() {
            return type;
        }

        public String getId() {
            return id;
        }

        public URLQueryHandler getUrlSetter() {
            return urlSetter;
        }

        public boolean isSingle() {
            return single;
        }

        public boolean isEditable() {
            return editable;
        }

        public boolean isRemoveable() {
            return fixed;
        }

    }
}
