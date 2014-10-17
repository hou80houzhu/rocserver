package com.rocui.comet.service.impl;

import com.rocui.comet.base.Comet.CometRouter;
import com.rocui.comet.base.CometResponse;
import com.rocui.comet.service.CometBaseService;
import java.util.HashMap;

public class MessageService extends CometBaseService {

    @Override
    public CometResponse excute() {
        CometResponse response = new CometResponse();
        try {
            String to = this.getCometRequest().getParameter("to").toString();
            String content = this.getCometRequest().getParameter("content").toString();
            response.add(content, CometRouter.ROUT_BRAODCAST, CometBaseService.SERVICE_MESSAGE);
            response.add("ok", CometRouter.ROUT_SELF, CometBaseService.SERVICE_SELF);
        } catch (Exception ex) {
            ex.printStackTrace();
            return response.add(null, CometRouter.ROUT_SELF, "0", CometBaseService.SERVICE_SELF);
        }
        return response;
    }

}
