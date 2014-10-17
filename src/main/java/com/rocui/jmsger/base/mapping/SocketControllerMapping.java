/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rocui.jmsger.base.mapping;

import com.rocui.jmsger.annotation.SocketController;
import com.rocui.util.base.PackageBrowser;
import com.rocui.util.base.reflect.ObjectSnooper;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author hou80houzhu
 */
public class SocketControllerMapping {

    private HashMap<String, String> mapping = new HashMap<String, String>();
    private static SocketControllerMapping x = null;
    private String path;

    private SocketControllerMapping(String path) throws Exception {
        this.path = path;
    }

    public static SocketControllerMapping getSocketControllerMapping(String path) throws Exception {
        if (SocketControllerMapping.x == null) {
            SocketControllerMapping.x = new SocketControllerMapping(path);
        }
        return SocketControllerMapping.x;
    }

    public SocketControllerMapping addMapping(String key, String path) {
        this.mapping.put(key, path);
        return this;
    }

    public void searchClass() throws Exception {
        String path = this.path;
        if (path != null && !path.equals("")) {
            List<String> classNames = PackageBrowser.getClassNames(path);
            if (classNames != null) {
                for (String classname : classNames) {
                    if (Class.forName(classname).isAnnotationPresent(SocketController.class)) {
                        String name = Class.forName(classname).getAnnotation(SocketController.class).name();
                        this.mapping.put(name, classname);
                    }
                }
            }
        }
    }

    public static SocketControllerMapping getSocketControllerMapping() {
        return SocketControllerMapping.x;
    }

    public Object getController(String controllername) throws Exception {
        return Class.forName(this.mapping.get(controllername)).newInstance();
    }

    public ObjectSnooper getWrapController(String controllername) throws Exception {
        return ObjectSnooper.snoop(this.mapping.get(controllername));
    }
}
