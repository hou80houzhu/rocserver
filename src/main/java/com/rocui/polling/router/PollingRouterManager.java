package com.rocui.polling.router;

import com.rocui.polling.annotation.PollingRouter;
import com.rocui.util.base.PackageBrowser;
import com.rocui.util.base.reflect.ObjectSnooper;
import java.util.HashMap;
import java.util.List;

public class PollingRouterManager {

    private static PollingRouterManager manager;
    private final HashMap<String, String> mapping;

    private PollingRouterManager(String path) throws Exception {
        this.mapping = new HashMap<>();
        this.mapping.put("broadcast", "com.rocui.polling.router.impl.BroadCastRouter");
        this.mapping.put("connect", "com.rocui.polling.router.impl.ConnectRouter");
        this.mapping.put("group", "com.rocui.polling.router.impl.GroupRouter");
        this.mapping.put("self", "com.rocui.polling.router.impl.SelfRouter");
        this.mapping.put("withoutgroup", "com.rocui.polling.router.impl.WithoutGroupRouter");
        this.mapping.put("withoutself", "com.rocui.polling.router.impl.WithoutSelfRouter");
        this.mapping.put("some", "com.rocui.polling.router.impl.SomeRouter");
        this.mapping.put("somegroup", "com.rocui.polling.router.impl.SomeGroupRouter");
        this.mapping.put("filter", "com.rocui.polling.router.impl.FilterRouter");
        if (path != null && !path.equals("")) {
            List<String> classNames = PackageBrowser.getClassNames(path);
            if (classNames != null) {
                for (String classname : classNames) {
                    if (Class.forName(classname).isAnnotationPresent(PollingRouter.class)) {
                        String name = Class.forName(classname).getAnnotation(PollingRouter.class).name();
                        this.mapping.put(name, classname);
                    }
                }
            }
        }
    }

    public static PollingRouterManager init(String path) throws Exception {
        if (null == manager) {
            manager = new PollingRouterManager(path);
        }
        return manager;
    }

    public static PollingRouterManager getManager() {
        return manager;
    }

    public BaseRouter getRouter(String type) throws Exception {
        String name = this.mapping.get(type);
        BaseRouter service = null;
        if (null != name) {
            Object obj = ObjectSnooper.snoop(name).object();
            if (null != obj && obj instanceof BaseRouter) {
                return (BaseRouter) obj;
            }
        }
        return service;
    }
}
