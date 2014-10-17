/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rocui.jmsger.service.imp;

import com.rocui.jmsger.base.connect.Connect;
import com.rocui.jmsger.base.result.ResultSet;
import com.rocui.jmsger.router.BaseSocketRouter;
import com.rocui.jmsger.service.BaseSocketService;
import com.rocui.jmsger.service.ClientServiceType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

/**
 *
 * @author hou80houzhu
 */
public class LoginService extends BaseSocketService {

    @Override
    public void doService() throws Exception {
        ResultSet result_one=this.getResultSet();
        result_one.setCode(ResultSet.RESULT_SUCCESS).setService(ClientServiceType.SERVICE_SELF).setData(this.getConnect().id()).setRoutName(BaseSocketRouter.ROUT_NAME_SIMPLE).setRoutType(BaseSocketRouter.ROUT_SELF);
        HashMap<String,Connect> cons=this.getConnections().getConnects();
        List<HashMap<String,String>> ids=new ArrayList<HashMap<String,String>>();
        for(Entry<String,Connect> map:cons.entrySet()){
            HashMap<String,String> mx=new HashMap<String, String>();
            mx.put("id", map.getKey());
            ids.add(mx);
        }
        HashMap<String,Object> r=new HashMap<String, Object>();
        r.put("who", this.getConnect().id());
        r.put("online", ids);
        ResultSet result_two=this.getResultSet();
        result_two.setCode(ResultSet.RESULT_SUCCESS).setData(r).setRoutName(BaseSocketRouter.ROUT_NAME_SIMPLE).setRoutType(BaseSocketRouter.ROUT_BRODCAST).setService(ClientServiceType.SERVICE_LOGIN);
        getResponse().addResult(result_one);
        getResponse().addResult(result_two);
    }
}
