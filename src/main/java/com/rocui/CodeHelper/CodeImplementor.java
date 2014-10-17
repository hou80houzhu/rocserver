package com.rocui.CodeHelper;

import com.rocui.boot.ServiceBootListener;
import com.rocui.util.jsonx.JsonEachArray;
import com.rocui.util.jsonx.Jsonx;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CodeImplementor {

    private static CodeImplementor implementor = null;
    private List<Helper> list = new ArrayList<>();

    private CodeImplementor() {
    }

    public static CodeImplementor getCodeImplementor() {
        if (null == implementor) {
            implementor = new CodeImplementor();
        }
        return implementor;
    }

    public void start() {
        File file = (new File(ServiceBootListener.class.getClassLoader().getResource("bootservice.json").getFile()));
        try {
            Jsonx json = Jsonx.create(file);
            json.get("services").each(new JsonEachArray() {
                @Override
                public boolean each(int index, Jsonx json) throws Exception {
                    String moduleName = json.get("name").toString();
                    Jsonx help = json.get("codeHelper");
                    String name = help.get("className").toString();
                    if (null != name && !name.equals("")) {
                        Helper helper = new Helper();
                        helper.modelName = moduleName;
                        helper.helperName = name;
                        helper.optionp = help.get("option");
                        helper.option = json.get("options");
                        helper.helper = (BaseHelper) Class.forName(name).newInstance();
                        list.add(helper);
                    }
                    return false;
                }
            });
            this.run();
        } catch (Exception ex) {
            Logger.getLogger(ServiceBootListener.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void run() {
        for (Helper helper : this.list) {
            try {
                helper.helper.excute(helper.optionp, helper.option);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class Helper {

        private String modelName;
        private String helperName;
        private Jsonx option;
        private Jsonx optionp;
        private BaseHelper helper;
    }
}
