/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rocui.jmsger.base.mapping;

import com.rocui.jmsger.annotation.SocketService;
import com.rocui.jmsger.service.BaseSocketService;
import com.rocui.util.base.PackageBrowser;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author hou80houzhu
 */
public class SocketServiceMapping {

    private HashMap<String, String> mapping=new HashMap<String, String>();
    private static SocketServiceMapping x = null;
    private String path;

    private SocketServiceMapping(String path) throws Exception {
        this.path=path;
    }

    public static SocketServiceMapping getSocketServiceMapping(String path) throws Exception {
        if (SocketServiceMapping.x == null) {
            SocketServiceMapping.x = new SocketServiceMapping(path);
        }
        return SocketServiceMapping.x;
    }
    
    public SocketServiceMapping addMapping(String key,String path){
        this.mapping.put(key, path);
        return this;
    }
    
    public void searchClass() throws Exception{
        String path=this.path;
        if (path != null && !path.equals("")) {
            List<String> classNames = PackageBrowser.getClassNames(path);
            if (classNames != null) {
                for (String classname : classNames) {
                    if (Class.forName(classname).isAnnotationPresent(SocketService.class)) {
                        String name = Class.forName(classname).getAnnotation(SocketService.class).name();
                        this.mapping.put(name, classname);
                    }
                }
            }
        }
    }
    
    public static SocketServiceMapping getSocketServiceMapping(){
        return SocketServiceMapping.x;
    }

    public BaseSocketService getService(String servicename) throws Exception {
        String path = this.mapping.get(servicename);
        if (null!=path && !path.equals("")) {
            return (BaseSocketService) Class.forName(path).newInstance();
        } else {
            return null;
        }
    }
}
