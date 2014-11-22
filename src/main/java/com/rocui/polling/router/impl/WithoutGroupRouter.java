package com.rocui.polling.router.impl;

import com.rocui.polling.base.ConnectManager.PollingConnect;
import com.rocui.polling.base.message.MessageWrapper;
import com.rocui.polling.router.BaseRouter;

public class WithoutGroupRouter extends BaseRouter {

    @Override
    public void rout(PollingConnect connect, MessageWrapper wrapper) throws Exception {
        connect.getConnectManager().broadCastWithOutId(connect.getId(), wrapper.getMessage());
    }

}
