package com.rocui.mvc.mapping;

import com.rocui.mvc.filter.ActionFilter;
import com.rocui.mvc.annotation.Filter;
import com.rocui.util.base.PackageBrowser;
import com.rocui.util.base.reflect.ObjectSnooper;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import org.apache.log4j.Logger;

public class FilterContainer {

    private static FilterContainer container = null;

    private FilterContainer() {
    }

    public static FilterContainer getContainer() {
        if (container == null) {
            container = new FilterContainer();
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
                        ObjectSnooper snooper = ObjectSnooper.snoop(classname);
                        if (null != snooper.annotation(Filter.class)) {
                            Filter k = (Filter) snooper.annotation(Filter.class);
                            if (snooper.object() instanceof ActionFilter) {
                                this.mapping.put(k.name(), classname);
                                Logger.getLogger(FilterContainer.class).info("filter:"+k.name());
//                                System.out.println("[filter] "+classname+":"+k.name());
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ActionFilter getFilterChain(String filterNames) {
        String[] names = filterNames.split("\\,");
        HashMap<String, String> p = new HashMap<>();
        for (String n : names) {
            if (null != n) {
                p.put(n, "");
            }
        }
        ActionFilter b = null;
        ActionFilter d = null;
        int i = 0;
        for (Entry<String, String> mp : p.entrySet()) {
            try {
                if (null != mp.getKey() && !"".equals(mp.getKey())) {
                    ActionFilter c = (ActionFilter) ObjectSnooper.snoop(this.mapping.get(mp.getKey())).object();
                    if (null != c) {
                        if (i == 0) {
                            b = c;
                            d = c;
                        } else {
                            b.setFilter(c);
                            b = c;
                        }
                        i++;
                    }
                }
            } catch (Exception e) {
            }
        }
        return d;
    }
}
