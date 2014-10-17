package com.rocui.mvc.data.jdbc.pool;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.rocui.util.jsonx.Jsonx;
import java.util.HashMap;

public class DBBoot {

    public static HashMap<String, String> map = new HashMap<String, String>();

    public DBBoot(Jsonx option) {
        try {
            Jsonx json = option;
            String urlis = json.get("jdbcUrl").toString();
            String drivclass = json.get("driverClass").toString();
            String useris = json.get("username").toString();
            String passwordis = json.get("password").toString();
            String minis = json.get("minPoolSize").toString();
            String maxis = json.get("maxPoolSize").toString();
            String tablePrefix = json.get("tablePrefix").toString();

            DBBoot.map.put("url", urlis);
            DBBoot.map.put("driver", drivclass);
            DBBoot.map.put("username", useris);
            DBBoot.map.put("password", passwordis);
            DBBoot.map.put("min", minis);
            DBBoot.map.put("max", maxis);
            DBBoot.map.put("tablePrefix", tablePrefix);

            ComboPooledDataSource ds = DBPool.getDataSource();
            ds.setDriverClass(drivclass);
            ds.setUser(useris);
            ds.setPassword(passwordis);
            ds.setMinPoolSize(Integer.parseInt(minis));
            ds.setMaxPoolSize(Integer.parseInt(maxis));
            ds.setJdbcUrl(urlis);
        } catch (Exception e) {
            System.out.println("::Init the DBPool is failed::");
        }
    }
}
