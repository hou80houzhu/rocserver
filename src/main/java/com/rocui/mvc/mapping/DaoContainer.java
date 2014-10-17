package com.rocui.mvc.mapping;

import com.rocui.mvc.annotation.DaoClass;
import com.rocui.util.base.PackageBrowser;
import com.rocui.util.base.reflect.ObjectSnooper;
import java.util.HashMap;
import java.util.List;
import org.apache.log4j.Logger;

public class DaoContainer {
    private static DaoContainer container=null;
    private HashMap<String,String> mapping=new HashMap<>();
    
    private DaoContainer(){
        
    }
    public static DaoContainer getCotnainer(){
        if(container==null){
            container=new DaoContainer();
            container.mapping.put("mysql","com.rocui.mvc.data.dao.MysqlDao");
        }
        return container;
    }
    public void start(String path) {
        try {
            if (path != null && !path.equals("")) {
                List<String> classNames = PackageBrowser.getClassNames(path);
                if (classNames != null) {
                    for (String classname : classNames) {
                        ObjectSnooper snooper = ObjectSnooper.snoop(classname);
                        if (null != snooper.annotation(DaoClass.class)) {
                            DaoClass k = (DaoClass) snooper.annotation(DaoClass.class);
                            this.mapping.put(k.name(), classname);
                            Logger.getLogger(DaoContainer.class).info("dao:"+k.name());
//                            System.out.println("[dao] "+classname+":"+k.name());
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public Object getDao(String daoName) throws Exception{
        String a=this.mapping.get(daoName);
        if(null!=a&&!"".equals(a)){
            return ObjectSnooper.snoop(a).object();
        }else{
            return null;
        }
    }
}
