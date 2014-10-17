package com.rocui.comet.service.controller;

import com.rocui.comet.annotation.CometController;
import com.rocui.util.base.PackageBrowser;
import java.util.HashMap;
import java.util.List;

public class CometControllerManager {

    private HashMap<String, String> classMapping = new HashMap<String, String>();
    private static CometControllerManager manager = null;

    private CometControllerManager(String path) throws Exception {
        if (path != null && !path.equals("")) {
            List<String> classNames = PackageBrowser.getClassNames(path);
            if (classNames != null) {
                for (String classname : classNames) {
                    if (Class.forName(classname).isAnnotationPresent(CometController.class)) {
                        String name = Class.forName(classname).getAnnotation(CometController.class).name();
                        this.classMapping.put(name, classname);
                    }
                }
            }
        }
    }

    public static void init(String path) throws Exception {
        if (CometControllerManager.manager == null) {
            CometControllerManager.manager = new CometControllerManager(path);
        }
    }

    public static CometControllerManager getManager() {
        return CometControllerManager.manager;
    }

    public CometBaseController getController(String controllerName) throws Exception {
        if (this.classMapping.containsKey(controllerName)) {
            String className = this.classMapping.get(controllerName);
            Object tx = Class.forName(className).newInstance();
            return (CometBaseController) tx;
        } else {
            return null;
        }
    }
}
