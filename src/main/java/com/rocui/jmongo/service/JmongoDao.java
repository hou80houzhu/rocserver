package com.rocui.jmongo.service;

import com.rocui.jmongo.Jmongo;
import com.rocui.jmongo.base.annotation.Dao;
import com.rocui.util.base.PackageBrowser;
import com.rocui.util.jsonx.Jsonx;
import java.io.File;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.List;

public class JmongoDao {

    private static HashMap<String, String> mapping = null;

    public static Object getDao(String className) throws Exception {
        String classNamex = JmongoDao.mappingDao(className);
        if (classNamex != null && !classNamex.equals("")) {
            Object xx = Class.forName(classNamex).newInstance();
            JmongoHandler hand = new JmongoHandler(xx);
            return Proxy.newProxyInstance(xx.getClass().getClassLoader(), xx.getClass().getInterfaces(), hand);
        } else {
            throw new Exception("dao with name is " + className + " can not find");
        }
    }

    public static Object getDaoPath(String className) throws Exception {
        Object xx = Class.forName(className).newInstance();
        JmongoHandler hand = new JmongoHandler(xx);
        return Proxy.newProxyInstance(xx.getClass().getClassLoader(), xx.getClass().getInterfaces(), hand);
    }

    private static String mappingDao(String classLiteName) throws Exception {
        if (JmongoDao.mapping == null) {
            File file = (new File(Jmongo.class.getClassLoader().getResource("database.json").getFile()));
            Jsonx json = Jsonx.create(file);
            String path = json.get("daoPath").toString();
            HashMap<String, String> mappingx = new HashMap<String, String>();
            List<String> classNames = PackageBrowser.getClassNames(path);
            if (classNames != null) {
                for (String classname : classNames) {
                    if (Class.forName(classname).isAnnotationPresent(Dao.class)) {
                        String name = Class.forName(classname).getAnnotation(Dao.class).name();
                        mappingx.put(name, classname);
                    }
                }
            }
            JmongoDao.mapping = mappingx;
        }
        return JmongoDao.mapping.get(classLiteName);
    }
}
