/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rocui.jmsger.service.imp;

import com.rocui.jmsger.base.result.ResultSet;
import com.rocui.jmsger.router.BaseSocketRouter;
import com.rocui.jmsger.service.BaseSocketService;
import com.rocui.jmsger.service.ClientServiceType;
import java.util.HashMap;

/**
 *
 * @author hou80houzhu
 */
public class LogoutService extends BaseSocketService {

    @Override
    public void doService() {
        ResultSet result = this.getResultSet();
        result.setCode(ResultSet.RESULT_SUCCESS);
        result.setService(ClientServiceType.SERVICE_LOGOUT);
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("who", request.getSessionId());
        result.setData(map);
        result.setRoutType(BaseSocketRouter.ROUT_WITHOUTSELF);
        result.setRoutName(BaseSocketRouter.ROUT_NAME_SIMPLE);
        response.addResult(result);
        connect.close();
    }
}
