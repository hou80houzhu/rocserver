package com.rocui.jpager;

import com.rocui.jpager.annotation.PageProvider;
import com.rocui.jpager.base.JpageProviderBase;
import com.rocui.util.base.PackageBrowser;
import java.util.HashMap;
import java.util.List;

public class JpageController {

    private HashMap<String, String> classMapping = new HashMap<String, String>();
    private static JpageController controller = null;

    public JpageController(String path) throws Exception {
        if (path != null && !path.equals("")) {
            List<String> classNames = PackageBrowser.getClassNames(path);
            if (classNames != null) {
                for (String classname : classNames) {
                    if (Class.forName(classname).isAnnotationPresent(PageProvider.class)) {
                        String name = Class.forName(classname).getAnnotation(PageProvider.class).name();
                        this.classMapping.put(name, classname);
                    }
                }
            }
        }
    }

    public static void init(String path) throws Exception {
        if (JpageController.controller == null) {
            JpageController.controller = new JpageController(path);
        }
    }

    public static JpageController get() {
        return JpageController.controller;
    }

    public String getClassName(String key) {
        return this.classMapping.get(key);
    }

    public JpageProviderBase getController(String key) throws Exception {
        String name = this.classMapping.get(key);
        if (name != null && name.length() > 0) {
            return (JpageProviderBase) Class.forName(name).newInstance();
        } else {
            return null;
        }
    }
}
