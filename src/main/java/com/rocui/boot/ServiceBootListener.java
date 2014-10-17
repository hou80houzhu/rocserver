package com.rocui.boot;

import com.rocui.util.jsonx.JsonEachArray;
import com.rocui.util.jsonx.Jsonx;
import java.io.File;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 *
 * @author Jinliang
 */
public class ServiceBootListener implements ServletContextListener {

    private static final HashMap<String, BootService> map = new HashMap<String, BootService>();

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        File file = (new File(ServiceBootListener.class.getClassLoader().getResource("bootservice.json").getFile()));
        try {
            Jsonx json = Jsonx.create(file);
            json.get("services").each(new JsonEachArray() {
                @Override
                public boolean each(int index, Jsonx json) {
                    try {
                        String className = json.get("className").toString();
                        String name = json.get("name").toString();
                        map.put(name, new BootService(name, className, (IBootService) Class.forName(className).newInstance(),json.get("options")));
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    return false;
                }
            });
        } catch (Exception ex) {
            Logger.getLogger(ServiceBootListener.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.boot();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        this.stop();
    }

    public static void stopService(String serviceName) {
        BootService n = null;
        for (Entry<String, BootService> x : map.entrySet()) {
            if (x.getKey().equals(serviceName)) {
                n = x.getValue();
            }
        }
        if (n != null) {
            n.getService().serviceStop();
            map.remove(n);
        }
    }

    private void boot() {
        for (Entry<String, BootService> x : map.entrySet()) {
            BootService service=x.getValue();
            service.getService().serviceStart(service.getOptions());
            System.out.println("[service boot: "+x.getKey()+" startup]");
        }
    }

    private void stop() {
        for (Entry<String, BootService> x : map.entrySet()) {
            x.getValue().getService().serviceStop();
            System.out.println("[service boot: "+x.getKey()+" shutdown]");
        }
    }

    public class BootService {

        private String name;
        private String className;
        private IBootService service;
        private Jsonx options;

        public BootService(String name, String className, IBootService service,Jsonx options) {
            this.name = name;
            this.className = className;
            this.service = service;
            this.options=options;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getClassName() {
            return className;
        }

        public void setClassName(String className) {
            this.className = className;
        }

        public IBootService getService() {
            return service;
        }

        public void setService(IBootService service) {
            this.service = service;
        }

        public Jsonx getOptions() {
            return options;
        }

        public void setOptions(Jsonx options) {
            this.options = options;
        }

    }
}
