package com.rocui.comet.service;

import com.rocui.comet.annotation.CometService;
import com.rocui.comet.service.impl.RPCService;
import com.rocui.comet.service.impl.MessageService;
import com.rocui.util.base.PackageBrowser;
import java.util.HashMap;
import java.util.List;

public class CometServiceManager {

    private HashMap<String, String> classMapping = new HashMap<String, String>();
    private static CometServiceManager manager = null;

    private CometServiceManager(String path) throws Exception {
        this.classMapping.put("message", MessageService.class.getName());
        this.classMapping.put("rpc", RPCService.class.getName());
        if (path != null && !path.equals("")) {
            List<String> classNames = PackageBrowser.getClassNames(path);
            if (classNames != null) {
                for (String classname : classNames) {
                    if (Class.forName(classname).isAnnotationPresent(CometService.class)) {
                        String name = Class.forName(classname).getAnnotation(CometService.class).name();
                        this.classMapping.put(name, classname);
                    }
                }
            }
        }
    }

    public static void init(String path) throws Exception {
        if (CometServiceManager.manager == null) {
            CometServiceManager.manager = new CometServiceManager(path);
        }
    }

    public static CometServiceManager getManager() {
        return CometServiceManager.manager;
    }

    public CometBaseService getService(String serviceName) throws Exception {
        if (this.classMapping.containsKey(serviceName)) {
            String className = this.classMapping.get(serviceName);
            Object tx = Class.forName(className).newInstance();
            return (CometBaseService) tx;
        } else {
            return null;
        }
    }
}
