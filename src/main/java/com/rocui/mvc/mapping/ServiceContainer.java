package com.rocui.mvc.mapping;

import com.rocui.mvc.annotation.Service;
import com.rocui.mvc.TransationHandler;
import com.rocui.util.base.PackageBrowser;
import com.rocui.util.base.reflect.ObjectSnooper;
import java.util.HashMap;
import java.util.List;
import org.apache.log4j.Logger;

public class ServiceContainer {

    private static ServiceContainer container = null;

    private ServiceContainer() {
    }

    public static ServiceContainer getContainer() {
        if (container == null) {
            container = new ServiceContainer();
        }
        return container;
    }

    private HashMap<String, String> mapping = new HashMap<>();

    protected void start(String path) {
        try {
            if (path != null && !path.equals("")) {
                List<String> classNames = PackageBrowser.getClassNames(path);
                if (classNames != null) {
                    for (String classname : classNames) {
                        if (!Class.forName(classname).isInterface()) {
                            ObjectSnooper snooper = ObjectSnooper.snoop(classname);
                            if (null != snooper.annotation(Service.class)) {
                                Service k = (Service) snooper.annotation(Service.class);
                                this.mapping.put(k.name(), classname);
                                Logger.getLogger(ServiceContainer.class).info("service:"+k.name());
//                                System.out.println("[service] "+classname+":"+k.name());
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Object getService(String serviceName) {
        String name = this.mapping.get(serviceName);
        if (null != name && !"".equals(name)) {
            try {
                ObjectSnooper snooper=ObjectSnooper.snoop(name);
                Object object = snooper.proxy(new TransationHandler());
                return object;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        } else {
            return null;
        }
    }
}
