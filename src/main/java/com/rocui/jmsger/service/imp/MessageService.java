/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rocui.jmsger.service.imp;

import com.rocui.jmsger.base.result.ResultSet;
import com.rocui.jmsger.router.BaseSocketRouter;
import com.rocui.jmsger.service.BaseSocketService;
import com.rocui.jmsger.service.ClientServiceType;

/**
 *
 * @author hou80houzhu
 */
public class MessageService extends BaseSocketService {

    @Override
    public void doService() {
        ResultSet result = this.getResultSet();
        result.setService(ClientServiceType.SERVICE_SELF);
        result.setCode(ResultSet.RESULT_SUCCESS);
        result.setData("{code:1}");
        result.setRoutType(BaseSocketRouter.ROUT_SELF);
        result.setRoutName(BaseSocketRouter.ROUT_NAME_SIMPLE);

        ResultSet result2 = this.getResultSet();
        result2.setService(ClientServiceType.SERVICE_MESSAGE);
        result2.setCode(ResultSet.RESULT_SUCCESS);
        result2.setData(request.getContent());
        result2.setFrom(request.getSessionId());
        result2.setRoutType(BaseSocketRouter.ROUT_TO);
        result2.setRoutName(BaseSocketRouter.ROUT_NAME_SIMPLE);
        result2.setTo(request.getTo());

        response.addResult(result);
        response.addResult(result2);
    }
}
