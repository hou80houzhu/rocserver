package com.rocui.polling.service.impl;

import com.rocui.polling.base.message.PollingMessage;
import com.rocui.polling.base.PollingRequest;
import com.rocui.polling.base.PollingResponse;
import com.rocui.polling.router.BaseRouter;
import com.rocui.polling.service.BaseService;
import com.rocui.util.jsonx.Jsonx;
import java.util.HashMap;

public class OneToOneService extends BaseService {

    @Override
    public void excute(PollingRequest request, PollingResponse response) {
        PollingMessage message = new PollingMessage();
        message.setCode("1");
        HashMap<String, String> map = new HashMap<>();
        map.put("form", request.getConnect().getId());
        map.put("msg", request.getParameter("content"));
        message.setContent(Jsonx.create(map).toString());
        message.setCallback("message");
        response.append(BaseRouter.CARRIER_TYPE_CONNECT, request.getParameter("to"), message);
    }

}
