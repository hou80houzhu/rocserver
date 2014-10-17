package com.rocui.mvc.mapping;

import com.rocui.mvc.annotation.Model;
import com.rocui.util.base.PackageBrowser;
import com.rocui.util.base.reflect.ObjectSnooper;
import java.util.HashMap;
import java.util.List;
import org.apache.log4j.Logger;

public class ModelContainer {

    private static ModelContainer container = null;

    private ModelContainer() {
    }

    public static ModelContainer getContainer() {
        if (container == null) {
            container = new ModelContainer();
        }
        return container;
    }
    private HashMap<String, String> mapping = new HashMap<>();

    public void start(String path) {
        try {
            if (path != null && !path.equals("")) {
                List<String> classNames = PackageBrowser.getClassNames(path);
                if (classNames != null) {
                    for (String classname : classNames) {
                        ObjectSnooper snooper = ObjectSnooper.snoop(classname);
                        if (null != snooper.annotation(Model.class)) {
                            Model k = (Model) snooper.annotation(Model.class);
                            String tableName = k.name();
                            this.mapping.put(tableName, classname);
                            Logger.getLogger(ModelContainer.class).info("model:"+ tableName);
//                            System.out.println("[model] " + classname + ":" + tableName);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public <T> T getModel(String modelName) throws Exception {
        String info = this.mapping.get(modelName);
        if (null != info) {
            return ObjectSnooper.snoop(info).instance();
        } else {
            return null;
        }
    }
}
