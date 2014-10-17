package com.rocui.jmsger.service.imp;

import com.rocui.jmsger.base.mapping.SocketControllerMapping;
import com.rocui.jmsger.base.result.ResultSet;
import com.rocui.jmsger.router.BaseSocketRouter;
import com.rocui.jmsger.service.BaseSocketService;
import com.rocui.jmsger.service.ClientServiceType;
import com.rocui.util.jsonx.Jsonx;

public class RPCService extends BaseSocketService {

    @Override
    public void doService() throws Exception {
        String objName = request.getObject();
        Object result = null;
        if (objName != null && !objName.equals("")) {
            String methodName = request.getMethod();
            if (methodName == null || methodName.equals("")) {
                methodName = "doAction";
            }
            String paras = request.getParameters();
            if (paras != null && !paras.equals("")) {
                result = SocketControllerMapping.getSocketControllerMapping().getWrapController(objName).set("request", request).set(Jsonx.create(paras).toHashMap()).trigger(methodName);
            } else {
                result = SocketControllerMapping.getSocketControllerMapping().getWrapController(objName).set("request", request).trigger(methodName);
            }
        }
        ResultSet rx = this.getResultSet();
        rx.setService(ClientServiceType.SERVICE_SELF).setData(result).setRoutName(BaseSocketRouter.ROUT_NAME_SIMPLE).setRoutType(BaseSocketRouter.ROUT_SELF).setCode(1).setFrom(request.getSessionId());
        response.addResult(rx);
    }

}
