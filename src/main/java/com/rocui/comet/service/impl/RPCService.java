package com.rocui.comet.service.impl;

import com.rocui.comet.base.Comet.CometRouter;
import com.rocui.comet.base.CometResponse;
import com.rocui.comet.service.CometBaseService;
import com.rocui.comet.service.controller.CometControllerManager;
import com.rocui.util.base.reflect.ObjectSnooper;
import com.rocui.util.jsonx.Jsonx;
import java.util.HashMap;

public class RPCService extends CometBaseService {

    @Override
    public CometResponse excute() {
        CometResponse response = new CometResponse();
        try {
            String controllerName = this.getCometRequest().getParameter("controller").toString();
            String methodName = this.getCometRequest().getParameter("method").toString();
            String parameter = this.getCometRequest().getParameter("parameter").toString();
            Object obj = CometControllerManager.getManager().getController(controllerName);
            if (obj != null) {
                ObjectSnooper x = ObjectSnooper.snoop(obj);
                if (parameter != null && parameter.trim().length() > 0) {
                    HashMap<String, Object> k = Jsonx.create(parameter).toHashMap();
                    x.set(k);
                }
                Object result = x.trigger(methodName);
                return response.add(result, CometRouter.ROUT_OFF_SELF, CometBaseService.SERVICE_SELF);
            } else {
                return response.add(null, CometRouter.ROUT_OFF_SELF, "0", CometBaseService.SERVICE_SELF);
            }
        } catch (Exception ex) {
            return response.add(null, CometRouter.ROUT_OFF_SELF, "0", CometBaseService.SERVICE_SELF);
        }
    }
}
