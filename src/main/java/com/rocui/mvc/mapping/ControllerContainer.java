package com.rocui.mvc.mapping;

import com.rocui.mvc.annotation.Controller;
import com.rocui.mvc.annotation.Action;
import com.rocui.mvc.annotation.ControllerService;
import com.rocui.mvc.annotation.Filters;
import com.rocui.mvc.annotation.Transation;
import com.rocui.mvc.mapping.URLFilter.MatchResult;
import com.rocui.util.base.PackageBrowser;
import com.rocui.util.base.reflect.ObjectSnooper;
import com.rocui.util.base.reflect.each.FieldAnnotaionEach;
import com.rocui.util.base.reflect.each.MethodAnnotaionEach;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import org.apache.log4j.Logger;

public class ControllerContainer {

    private static ControllerContainer container = null;
    private final HashMap<String, ControllerInfo> controllerMapping = new HashMap<>();
    private final URLFilter filter = new URLFilter();
    private String excludesuffix = "";

    private ControllerContainer() {
    }

    public static ControllerContainer getContainer() {
        if (ControllerContainer.container == null) {
            ControllerContainer.container = new ControllerContainer();
            StringBuilder sb = new StringBuilder();
            sb.append("^.+\\.");
            String[] exclude = BaseMapping.mapping.get("exclude").toString().split("\\,");
            sb.append("(");
            for (String a : exclude) {
                sb.append(a);
                sb.append("|");
            }
            sb.deleteCharAt(sb.length() - 1);
            sb.append(")$");
            container.excludesuffix = sb.toString();
        }
        return ControllerContainer.container;
    }

    public ControllerInfo getInfo(String uri) {
        ControllerInfo c = null;
        MatchResult result = this.filter.check(uri);
        c = this.controllerMapping.get(result.getOriginalPath());
        if (null != c) {
            c.urlpars = result.getValueMap();
            if (result.isHasParas()) {
                c.hasParas = true;
            }
        }
        return c;
    }

    protected void start(String path) {
        try {
            if (path != null && !path.equals("")) {
                List<String> classNames = PackageBrowser.getClassNames(path);
                if (classNames != null) {
                    for (String classname : classNames) {
                        ObjectSnooper snooper = ObjectSnooper.snoop(classname);
                        final String classnamex = classname;
                        Object filt = snooper.annotation(Filters.class);
                        final String classFilters = filt != null ? ((Filters) filt).filters() : "";
                        if (null != snooper.annotation(Controller.class)) {
                            final String basepath = ((Controller) snooper.annotation(Controller.class)).basePath();
                            final HashMap<String, String> servicemap = new HashMap<>();
                            snooper.fieldAnnotaions(ControllerService.class, new FieldAnnotaionEach(servicemap) {
                                @Override
                                public boolean each(Field field, Annotation annotion) throws Exception {
                                    HashMap<String, String> map = (HashMap<String, String>) arguments[0];
                                    map.put(field.getName(), ((ControllerService) annotion).name());
                                    return false;
                                }
                            });
                            Logger.getLogger(ControllerContainer.class).info("controller:" + classname);
                            snooper.methodAnnotaions(Action.class, new MethodAnnotaionEach(this.controllerMapping, servicemap, filter) {
                                @Override
                                public boolean each(Method method, Annotation annotion) throws Exception {
                                    HashMap<String, ControllerInfo> mapping = (HashMap<String, ControllerInfo>) arguments[0];
                                    URLFilter filter = (URLFilter) arguments[2];
                                    ControllerInfo info = new ControllerInfo();
                                    if (method.isAnnotationPresent(Transation.class)) {
                                        info.transation = true;
                                    };
                                    String path = ((Action) annotion).path();
                                    String key = basepath + path;
                                    info.actionName = method.getName();
                                    info.controllerName = classnamex;
                                    info.services = (HashMap<String, String>) arguments[1];
                                    Filters aa = method.getAnnotation(Filters.class);
                                    String methodFilters = "";
                                    if (null != aa) {
                                        methodFilters = aa.filters();
                                    }
                                    info.filters = ("".equals(classFilters) ? "" : classFilters + ",") + methodFilters;
                                    mapping.put((key.endsWith("/") ? key : key + "/"), info);
                                    Logger.getLogger(ControllerContainer.class).info("--action:" + (key.endsWith("/") ? key : key + "/"));
                                    filter.add(key);
                                    return false;
                                }
                            });
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class ControllerInfo {

        private String controllerName;
        private String actionName;
        private String filters;
        private boolean transation = false;
        private HashMap<String, String> urlpars = null;
        private boolean hasParas = false;

        private HashMap<String, String> services = new HashMap<>();

        public String getControllerName() {
            return this.controllerName;
        }

        public String getActionName() {
            return this.actionName;
        }

        public String getFilters() {
            return this.filters;
        }

        public boolean isTransation() {
            return this.transation;
        }

        public HashMap<String, String> getServices() {
            return this.services;
        }

        public void addService(String filedName, String serviceName) {
            this.services.put(filedName, serviceName);
        }

        public HashMap<String, String> getURLpars() {
            return this.urlpars;
        }

        public boolean isHasParas() {
            return hasParas;
        }
    }
}
