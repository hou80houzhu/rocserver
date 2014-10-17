package com.rocui.jmsger.router.imp;

import com.rocui.jmsger.base.connect.Connect;
import com.rocui.jmsger.base.connect.ConnectCollection;
import com.rocui.jmsger.base.result.ResultSet;
import com.rocui.jmsger.router.BaseSocketRouter;
import com.rocui.util.jsonx.Jsonx;

public class GroupRouter extends BaseSocketRouter {

    @Override
    public void doRout(ResultSet resultset, Connect connect, ConnectCollection connections) {
        String type = resultset.getRoutType();
        if (type.equals(BaseSocketRouter.ROUT_SELF)) {
            connect.send(Jsonx.create(resultset.getResult()).toString());
        } else if (type.equals(BaseSocketRouter.ROUT_BRODCAST)) {
            connections.broadCastMessage(Jsonx.create(resultset.getResult()).toString());
        } else if (type.equals(BaseSocketRouter.ROUT_TO)) {
            connections.sendToMessage(resultset.getTo(), Jsonx.create(resultset.getResult()).toString());
        } else if (type.equals(BaseSocketRouter.ROUT_WITHOUTSELF)) {
            connections.sendMessageWidthOut(connect.id(), Jsonx.create(resultset.getResult()).toString());
        }
    }

}
