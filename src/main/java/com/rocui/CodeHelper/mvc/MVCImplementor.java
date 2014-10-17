package com.rocui.CodeHelper.mvc;

import com.rocui.CodeHelper.BaseHelper;
import com.rocui.mvc.TransationHandler;
import com.rocui.mvc.data.jdbc.pool.DBBoot;
import com.rocui.util.base.reflect.ObjectSnooper;
import com.rocui.util.file.Jile;
import com.rocui.util.jsonx.Jsonx;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import org.apache.log4j.Logger;

public class MVCImplementor extends BaseHelper {

    @Override
    public void excute(Jsonx helpOption, Jsonx json) {
        try {
            new DBBoot(json.get("jdbc"));

            String dbname = helpOption.get("dataBaseName").toString();
            String modelPackage = json.get("model").toString();
            String controllerPackage=json.get("controller").toString();
            String servicePackage = json.get("service").toString();
            String codePath = helpOption.get("codePath").toString();
            String modelPath = codePath + modelPackage.replaceAll("\\.", "\\\\") + "\\";
            String servicePath = codePath + servicePackage.replaceAll("\\.", "\\\\") + "\\";
            String controllerPath=codePath+controllerPackage.replaceAll("\\.", "\\\\") + "\\";

            ObjectSnooper snooper = ObjectSnooper.snoop("com.rocui.CodeHelper.mvc.DBInfo");
            IDBInfo object = (IDBInfo) snooper.proxy(new TransationHandler());

            List<HashMap<String, Object>> tables = object.getTables(dbname);
            for (HashMap<String, Object> map : tables) {
                String table = map.get("Tables_in_" + dbname).toString();
                List<HashMap<String, Object>> cols = object.getTableInfo(dbname, table);
                Logger.getLogger(MVCImplementor.class).info("[build models] " + table);
                File file = (new File(MVCImplementor.class.getClassLoader().getResource("bootservice.json").getFile()));
                String path = file.getParentFile().getAbsolutePath() + "\\com\\rocui\\CodeHelper\\mvc\\class.flt";
                String idis = "";
                for (HashMap<String, Object> t : cols) {
                    if (t.get("Key").toString().equals("PRI")) {
                        idis = t.get("Field").toString();
                        break;
                    }
                }
                HashMap<String, Object> vars = new HashMap<>();
                vars.put("tableName", table);
                vars.put("fields", cols);
                vars.put("package", modelPackage);
                vars.put("ids", idis);
                String aa = this.parseTemplte(path, vars);
                Jile.with(modelPath + (table.substring(0, 1).toUpperCase() + table.substring(1)) + ".java").write(aa);
                
                file = (new File(MVCImplementor.class.getClassLoader().getResource("bootservice.json").getFile()));
                path = file.getParentFile().getAbsolutePath() + "\\com\\rocui\\CodeHelper\\mvc\\controller.flt";
                vars.put("package", controllerPackage);
                aa = this.parseTemplte(path, vars);
                Jile.with(controllerPath + (table.substring(0, 1).toUpperCase() + table.substring(1)) + "Controller.java").write(aa);
            }
            for (HashMap<String, Object> map : tables) {
                String table = map.get("Tables_in_ftlcarrecoinfo").toString();
                File file = (new File(MVCImplementor.class.getClassLoader().getResource("bootservice.json").getFile()));
                String path = file.getParentFile().getAbsolutePath() + "\\com\\rocui\\CodeHelper\\mvc\\service.flt";
                HashMap<String, Object> vars = new HashMap<>();
                vars.put("tableName", table);
                vars.put("package", servicePackage);
                String aa = this.parseTemplte(path, vars);
                Jile.with(servicePath + (table.substring(0, 1).toUpperCase() + table.substring(1)) + "Service.java").write(aa);
            }
            for (HashMap<String, Object> map : tables) {
                String table = map.get("Tables_in_" + dbname).toString();
                File file = (new File(MVCImplementor.class.getClassLoader().getResource("bootservice.json").getFile()));
                String path = file.getParentFile().getAbsolutePath() + "\\com\\rocui\\CodeHelper\\mvc\\serviceImp.flt";
                HashMap<String, Object> vars = new HashMap<>();
                vars.put("tableName", table);
                vars.put("package", servicePackage);
                String aa = this.parseTemplte(path, vars);
                Jile.with(servicePath + "serviceImp\\" + (table.substring(0, 1).toUpperCase() + table.substring(1)) + "ServiceImp.java").write(aa);
            }
        } catch (Exception ex) {
        }
    }
}
