package com.rocui.jpager;

import com.rocui.jpager.base.Jpager;
import com.rocui.jpager.base.Jpager.Jtemp;
import com.rocui.jpager.base.URLQueryHandler;
import com.rocui.util.base.Jdate;
import com.rocui.util.base.reflect.ObjectSnooper;
import com.rocui.util.base.string.Jstring;
import com.rocui.util.file.Jile;
import com.rocui.util.file.JileEach;
import com.rocui.util.jsonx.JsonEachArray;
import com.rocui.util.jsonx.Jsonx;
import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class JpageContainer {

    public static String PAGE_TYPE_SYSTEM = "system";
    public static String PAGE_TYPE_USER = "user";

    private static JpageContainer builder = null;

    private List<Jfolder> folders = new ArrayList<Jfolder>();
    private HashMap<String, Jpage> mapping = new HashMap<String, Jpage>();

    private HttpServletRequest request;
    private HashMap<String, Object> common;
    private String basepath;

    public JpageContainer(String path) throws Exception {
        this.basepath = path;
        Jsonx json = Jsonx.create(new File(System.getProperty("user.dir") + path + File.separator + "pages.json"));
        this.common = json.get("common").toHashMap();

        json.get("folder").each(new JsonEachArray(this) {
            @Override
            public boolean each(int index, Jsonx json) {
                JpageContainer ths = (JpageContainer) this.arguments[0];
                try {
                    Jfolder xfolder = new Jfolder(json.get("name").toString(), json.get("title").toString());
                    ths.folders.add(xfolder);
                    json.get("pages").each(new JsonEachArray(ths, xfolder) {
                        @Override
                        public boolean each(int index, Jsonx json) {
                            JpageContainer ths = (JpageContainer) this.arguments[0];
                            Jfolder fx = (Jfolder) this.arguments[1];
                            try {
                                String pagename = json.get("name").toString();
                                Jpage page = json.toObject(new Jpage(pagename));
                                page.setTitle(json.hasKey("title") ? json.get("title").toString() : "new page");
                                page.setAuthor(json.hasKey("author") ? json.get("author").toString().equals("") ? common.get("author").toString() : json.get("author").toString() : common.get("ahthor").toString());
                                page.setContent(json.hasKey("content") ? json.get("content").toString().equals("") ? common.get("content").toString() : json.get("content").toString() : common.get("content").toString());
                                page.setDesc(json.hasKey("desc") ? json.get("desc").toString().equals("") ? common.get("desc").toString() : json.get("desc").toString() : common.get("desc").toString());
                                page.setLogo(json.hasKey("logo") ? json.get("logo").toString().equals("") ? common.get("logo").toString() : json.get("logo").toString() : common.get("logo").toString());
                                page.setPagetitle(json.hasKey("pagetitle") ? json.get("pagetitle").toString().equals("") ? common.get("pagetitle").toString() : json.get("pagetitle").toString() : common.get("pagetitle").toString());
                                page.setKeywords(json.hasKey("keywords") ? json.get("keywords").toString().equals("") ? common.get("keywords").toString() : json.get("keywords").toString() : common.get("keywords").toString());
                                page.editable = json.hasKey("editable") ? json.get("editable").toBoolean() : true;
                                page.setType(json.hasKey("type") ? json.get("type").toString() : "system");
                                ths.mapping.put(pagename, page);
                                fx.addPage(pagename, page);
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                            return false;
                        }
                    });
                } catch (Exception ex) {
                }
                return false;
            }

        });

        json.get("pages").each(new JsonEachArray(this) {
            @Override
            public boolean each(int index, Jsonx json) {
                try {
                    JpageContainer ths = (JpageContainer) this.arguments[0];
                    String pagename = json.get("name").toString();
                    Jpage page = json.toObject(new Jpage(pagename));
                    ObjectSnooper.snoop(page).set(common);
                    ths.mapping.put(pagename, page);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                return false;
            }
        });
    }

    public static void init(String path) throws Exception {
        if (JpageContainer.builder == null) {
            JpageContainer.builder = new JpageContainer(path);
        }
    }

    public static JpageContainer getContainer() {
        return JpageContainer.builder;
    }

    public Jpage editPage(String pageName, HashMap<String, Object> attributes) throws Exception {
        if (this.mapping.containsKey(pageName)) {
            Jpage page = this.mapping.get(pageName);
            ObjectSnooper.snoop(page).set(attributes);
            return page;
        } else {
            return null;
        }
    }

    public Jpage editPageTitle(String pagename, String pagetitle) {
        if (this.mapping.containsKey(pagename)) {
            this.mapping.get(pagename).setTitle(pagetitle);
            return this.mapping.get(pagename);
        } else {
            return null;
        }
    }

    public HashMap<String, Object> editCommonConfig(HashMap<String, Object> commonMap) {
        for (Entry<String, Object> map : commonMap.entrySet()) {
            String key = map.getKey();
            if (this.common.containsKey(key)) {
                this.common.put(key, map.getValue());
            }
        }
        return this.common;
    }

    public HashMap<String, Object> getCommonConfig() {
        HashMap<String, Object> mapx = new HashMap<String, Object>();
        for (Entry<String, Object> map : common.entrySet()) {
            mapx.put(map.getKey(), map.getValue());
        }
        return mapx;
    }

    public Jpage getPage(String pageName) throws Exception {
        Jpage page = this.mapping.get(pageName);
        if (page == null) {
            page = this.mapping.get("404");
        } else {
            page.reset();
        }
        return page;
    }

    public List<HashMap<String, Object>> getPageList() {
        List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
        for (Entry<String, Jpage> map : this.mapping.entrySet()) {
            list.add(map.getValue().toMap());
        }
        return list;
    }

    public List<HashMap<String, Object>> getFolderPageList() {
        List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
        for (Jfolder fol : this.folders) {
            list.add(fol.getInfo());
        }
        return list;
    }

    public Jpage createPage(String folderName, String pageTitle) throws Exception {
        Jfolder folder = this.getFolder(folderName);
        if (folder == null) {
            folder = this.getFolder("customer");
        }
        Jpage page = this.createPage(pageTitle);
        folder.addPage(page.name, page);
        return page;
    }

    public Jfolder createFolder(String title) {
        Jfolder folder = new Jfolder(System.currentTimeMillis() + "", title);
        this.folders.add(folder);
        return folder;
    }

    public Jfolder renameFolder(String folderName, String folderTitle) {
        Jfolder folder = this.getFolder(folderName);
        if (folder != null) {
            folder.title = folderTitle;
            return folder;
        } else {
            return null;
        }
    }

    public boolean deleteFolder(String folderName) {
        boolean ok = false;
        Jfolder folder = this.getFolder(folderName);
        if (folder != null) {
            try {
                for (Entry<String, Jpage> page : folder.pages.entrySet()) {
                    this.removePage(page.getKey());
                }
                folder.pages.clear();
                this.folders.remove(folder);
                ok = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return ok;
    }

    private Jfolder getFolder(String folderName) {
        Jfolder x = null;
        for (Jfolder floder : this.folders) {
            if (floder.name.equals(folderName)) {
                x = floder;
                break;
            }
        }
        return x;
    }

    public List<HashMap<String, String>> getFolderList() {
        List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
        for (Jfolder folder : this.folders) {
            if (!folder.name.equals("system")) {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("name", folder.title);
                map.put("value", folder.name);
                list.add(map);
            }
        }
        return list;
    }

    private Jpage createPage(String title) throws Exception {
        String pageName = System.currentTimeMillis() + "";
        Jsonx config = Jpager.getPager().getConfig();
        Jile file = Jile.with(System.getProperty("user.dir") + config.get("pagePath").toString() + File.separator + pageName + "." + config.get("suffix"));
        if (!file.file().exists()) {
            Jile.with(System.getProperty("user.dir") + config.get("pagePath").toString() + File.separator + "base." + config.get("suffix"))
                    .copy(System.getProperty("user.dir") + config.get("pagePath").toString() + File.separator + pageName + "." + config.get("suffix"));
        }
        Jpage page = new Jpage(pageName);
        page.setType(JpageContainer.PAGE_TYPE_USER);
        ObjectSnooper.snoop(page).set(common);
        page.setTitle(title);
        this.mapping.put(pageName, page);
        return page;
    }

    public Jpage clonePage(String oname, String ntitle, String folderName) throws Exception {
        String pageName = System.currentTimeMillis() + "";
        Jsonx config = Jpager.getPager().getConfig();
        Jile file = Jile.with(System.getProperty("user.dir") + config.get("pagePath").toString() + File.separator + pageName + "." + config.get("suffix"));
        if (!file.file().exists()) {
            Jile.with(System.getProperty("user.dir") + config.get("pagePath").toString() + File.separator + oname + "." + config.get("suffix"))
                    .copy(System.getProperty("user.dir") + config.get("pagePath").toString() + File.separator + pageName + "." + config.get("suffix"));
        }
        Jpage page = new Jpage(pageName);
        page.setType(JpageContainer.PAGE_TYPE_USER);
        ObjectSnooper.snoop(page).set(common);
        page.setTitle(ntitle);
        this.mapping.put(pageName, page);

        Jfolder folder = this.getFolder(folderName);
        if (folder == null) {
            folder = this.getFolder("customer");
        }
        folder.addPage(page.name, page);

        return page;
    }

    public boolean deletePage(String pageName) {
        boolean ok = false;
        if (this.mapping.containsKey(pageName)) {
            this.mapping.remove(pageName);
            for (Jfolder folder : this.folders) {
                Jpage xpage = null;
                for (Entry<String, Jpage> page : folder.pages.entrySet()) {
                    if (page.getValue().name.equals(pageName)) {
                        xpage = page.getValue();
                        break;
                    }
                }
                if (xpage != null) {
                    folder.pages.remove(xpage.name);
                }
            }
            try {
                this.removePage(pageName);
                ok = true;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return ok;
    }

    public boolean deletePage(String folderName, String pageName) {
        boolean ok = false;
        Jfolder folder = this.getFolder(folderName);
        if (folder != null) {
            if (folder.pages.containsKey(pageName)) {
                try {
                    folder.pages.remove(pageName);
                    this.removePage(pageName);
                    ok = true;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    private JpageContainer removePage(String pageName) throws Exception {
        if (this.mapping.containsKey(pageName)) {
            this.mapping.remove(pageName);
            Jsonx config = Jpager.getPager().getConfig();
            Jile.with(config.get("pagePath").toString() + File.separator + pageName + "." + config.get("suffix")).remove();
        }
        return this;
    }

    public JpageContainer removeCache(String pageName) {
        if (pageName != null && !pageName.equals("")) {
            Jile.with(Jpager.getPager().getCachePath() + File.separator + pageName).remove();
        }
        return this;
    }

    public JpageContainer removeCache() {
        Jile.with(Jpager.getPager().getCachePath()).empty();
        return this;
    }

    public JpageContainer setCommonAttribute(String key, String value) {
        this.common.put(key, value);
        return this;
    }

    public void editPageContent(String pagename, String content) {
        try {
            Jsonx config = Jpager.getPager().getConfig();
            Jile file = Jile.with(System.getProperty("user.dir") + basepath + File.separator + pagename + "." + config.get("suffix"));
            if (file.file().exists()) {
                String cd = file.read();
                Matcher m = Pattern.compile("(?is)<body(.*?)>(.*?)</body>").matcher(cd);
                if (m.find()) {
                    StringBuilder sb = new StringBuilder();
                    int start = m.toMatchResult().start();
                    char end = cd.charAt(start);
                    while (end != '>') {
                        end = cd.charAt(start);
                        sb.append(end);
                        start += 1;
                    }
                    String contentxt = m.replaceAll(sb.toString() + content + "</body>");
                    file.write(contentxt);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.syBackup();
    }

    public void save() throws Exception {
        List<HashMap<String, Object>> list = this.getPageList();
        Jsonx json = Jsonx.create(new File(System.getProperty("user.dir") + basepath + File.separator + "pages.json"));
        json.set("pages", list).toFile(System.getProperty("user.dir") + basepath + File.separator + "pages.json");
    }

    private List<String> getAllPagesModuleIds() {
        try {
            List<String> list = new ArrayList<String>();
            Jile.with(System.getProperty("user.dir") + basepath).each(new JileEach(list) {
                @Override
                public boolean each(Jile file) {
                    List<String> list = (List<String>) this.arguments[0];
                    if (file.file().isFile() && file.file().getName().endsWith("html")) {
                        try {
                            String content = file.read();
                            Matcher m = Pattern.compile("(.*?)<@module(.*?)/>(.*?)").matcher(content);
                            while (m.find()) {
                                String moduleid = "";
                                Matcher mc = Pattern.compile("id=['\"](.*?)['\"]").matcher(m.group().toString());
                                while (mc.find()) {
                                    String ct = mc.group().toString();
                                    String[] cts = ct.split("=");
                                    moduleid = cts[1].substring(1, cts[1].length() - 1);
                                }
                                list.add(moduleid);
                            }
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                    return false;
                }
            });
            return list;
        } catch (Exception e) {
            return null;
        }
    }

    private void cleanActiveModules() {
        HashMap<String, HashMap<String, Object>> map = JmoduleContainer.getContainer().getActiveValuesMap();
        List<String> ids = this.getAllPagesModuleIds();
        HashMap<String, HashMap<String, Object>> mapx = new HashMap<String, HashMap<String, Object>>();
        for (String id : ids) {
            if (map.containsKey(id)) {
                mapx.put(id, map.get(id));
            }
            JmoduleContainer.getContainer().setActiveValuesMap(mapx);
        }
    }

    protected void backup() throws Exception {
        this.cleanAndSave();
        String bp = Jpager.getPager().getConfig().get("backupPath").toString();
        Jile.with(System.getProperty("user.dir") + basepath).zip(System.getProperty("user.dir") + bp + File.separator + System.currentTimeMillis() + ".zip");
    }

    protected void cleanAndSave() throws Exception {
        this.cleanActiveModules();
        JmoduleContainer.getContainer().save();
        this.save();
    }

    private void syBackup() {
        new Thread(new Runnable() {
            public void run() {
                System.out.println("[===================clean and save all====================]");
                try {
                    JpageContainer.getContainer().cleanAndSave();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }).start();
    }

    public class Jfolder {

        private String name;
        private String title;
        private HashMap<String, Jpage> pages = new HashMap<String, Jpage>();

        public Jfolder(String name, String title) {
            this.name = name;
            this.title = title;
        }

        public Jfolder addPage(String pageName, Jpage page) {
            this.pages.put(pageName, page);
            return this;
        }

        public Jpage getPage(String pageName) {
            return this.pages.get(pageName);
        }

        public HashMap<String, Object> getInfo() {
            HashMap<String, Object> info = new HashMap<String, Object>();
            info.put("name", this.name);
            info.put("title", this.title);
            List<HashMap<String, Object>> pgs = new ArrayList<HashMap<String, Object>>();
            for (Entry<String, Jpage> x : this.pages.entrySet()) {
                if (x.getValue() != null) {
                    pgs.add(x.getValue().toMap());
                }
            }
            info.put("pages", pgs);
            return info;
        }
    }

    public class Jpage {

        private String name;
        private String title;
        private String desc;
        private String content;
        private String keywords;
        private String author;
        private String logo;
        private String type;
        private boolean editable;
        private String pagetitle;

        private Jtemp pager;

        public Jpage(String pagename) throws Exception {
            this.name = pagename;
            this.pager = Jpager.getPager().getTemplate(pagename);
        }

        public Jpage reset() throws Exception {
            this.pager = Jpager.getPager().getTemplate(this.name);
            return this;
        }

        public Jtemp addAttribute(String key, Object value) {
            pager.addSessionAttribute(key, value);
            return pager;
        }

        public Jtemp addVariable(String key, Object value) {
            pager.append(key, value);
            return pager;
        }

        public Jtemp setRequest(HttpServletRequest request) {
            JpageContainer.this.request = request;
            URLQueryHandler x = new URLQueryHandler(request);
            this.addAttribute("request", request);
            this.addAttribute("urlSetter", x);
            return pager;
        }

        public Jtemp addVariables(HashMap<String, Object> map) {
            pager.append(map);
            return pager;
        }

        private void setPageInfo() {
            pager.append("Page", this.toMap());
        }

        public String getString() {
            this.setPageInfo();
            return pager.getContent();
        }

        private HashMap<String, String> getPageInfoByUrl(HttpServletRequest request) {
            HashMap<String, String> info = new HashMap<String, String>();
            String[] kk = request.getRequestURI().split("/");
            String qd = request.getQueryString();
            info.put("pageName", kk[kk.length - 1].split("\\.")[0]);
            info.put("queryString", qd != null && qd.length() > 0 ? qd : "noquery");
            return info;
        }

        public Jpage out(Writer writer) throws Exception {
            this.setPageInfo();
            pager.out(writer);
            return this;
        }

        public Jpage out(HttpServletResponse response) throws Exception {
            response.setContentType("text/html;charset=GBK");
            Writer writer = response.getWriter();
            String url = request.getRequestURL().toString();
            HashMap<String, String> info = this.getPageInfoByUrl(request);
            String pageName = info.get("pageName");
            String name = info.get("queryString");
            name = Jstring.with(name).md5().toString();
            HashMap<String, Long> list = Jpager.getPager().getCacheList();
            if (list.containsKey(pageName)) {
                Jile file = Jile.with(Jpager.getPager().getCachePath() + File.separator + pageName + File.separator + name + ".html");
                if (file.file().exists()) {
                    if (Jdate.now().toLong() - file.file().lastModified() > list.get(pageName)) {
                        file.write(this.getString());
                        pager.out(writer);
                    } else {
                        System.out.println("----->>>>>>>>>from cache<<<<<<<<<------");
                        file.read(writer);
                    }
                } else {
                    file.write(this.getString());
                    pager.out(writer);
                }
            } else {
                this.setPageInfo();
                pager.out(writer);
            }
            return this;
        }

        public String getName() {
            return name;
        }

        public String getTitle() {
            return title;
        }

        public String getPageTile() {
            return this.pagetitle;
        }

        public String getDesc() {
            return desc;
        }

        public String getContent() {
            return content;
        }

        public String getKeywords() {
            return keywords;
        }

        public String getAuthor() {
            return author;
        }

        public String getLogo() {
            return logo;
        }

        public String getType() {
            return type;
        }

        public boolean getEditable() {
            return editable;
        }

        public HashMap<String, Object> toMap() {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("name", name);
            map.put("title", title);
            map.put("desc", desc);
            map.put("pagetitle", pagetitle);
            map.put("content", content);
            map.put("keywords", keywords);
            map.put("author", author);
            map.put("logo", logo);
            map.put("type", type);
            map.put("editable", editable);
            return map;
        }

        private void setName(String name) {
            this.name = name;
        }

        private void setTitle(String title) {
            this.title = title;
        }

        private void setDesc(String desc) {
            this.desc = desc;
        }

        private void setContent(String content) {
            this.content = content;
        }

        private void setKeywords(String keywords) {
            this.keywords = keywords;
        }

        private void setAuthor(String author) {
            this.author = author;
        }

        private void setLogo(String logo) {
            this.logo = logo;
        }

        private void setType(String type) {
            this.type = type;
        }

        private void setPagetitle(String pagetitle) {
            this.pagetitle = pagetitle;
        }
    }

}
