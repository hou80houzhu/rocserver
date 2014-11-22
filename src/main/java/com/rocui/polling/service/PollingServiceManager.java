package com.rocui.polling.service;

import com.rocui.polling.annotation.PollingService;
import com.rocui.util.base.PackageBrowser;
import com.rocui.util.base.reflect.ObjectSnooper;
import java.util.HashMap;
import java.util.List;

public class PollingServiceManager {

    private static PollingServiceManager manager;
    private final HashMap<String, String> mapping;

    private PollingServiceManager(String path) throws Exception {
        this.mapping = new HashMap<>();
        this.mapping.put("onetoone", "com.rocui.polling.service.impl.OneToOneService");
        this.mapping.put("grouptogroup", "com.rocui.polling.service.impl.GroupToGroupService");
        if (path != null && !path.equals("")) {
            List<String> classNames = PackageBrowser.getClassNames(path);
            if (classNames != null) {
                for (String classname : classNames) {
                    if (Class.forName(classname).isAnnotationPresent(PollingService.class)) {
                        String name = Class.forName(classname).getAnnotation(PollingService.class).name();
                        this.mapping.put(name, classname);
                    }
                }
            }
        }
    }

    public static PollingServiceManager init(String path) throws Exception {
        if (null == manager) {
            manager = new PollingServiceManager(path);
        }
        return manager;
    }

    public static PollingServiceManager getManager() {
        return manager;
    }

    public BaseService getService(String type) throws Exception {
        String name = this.mapping.get(type);
        BaseService service = null;
        if (null != name) {
            Object obj = ObjectSnooper.snoop(name).object();
            if (null != obj && obj instanceof BaseService) {
                return (BaseService) obj;
            }
        }
        return service;
    }
}
