package com.rocui.comet.base.handler;

import com.rocui.comet.base.CometConnectCenter;
import com.rocui.comet.base.CometConnectCenter.CometConnect;
import com.rocui.comet.base.CometResultSet;
import java.util.List;

public class SimpleEventHandler extends CometBaseHandler {

    @Override
    public void onConnect(String id) {
        System.out.println("[comet connected:] id->" + id);
    }

    @Override
    public void onClose(CometConnectCenter.CometConnect connect) {
        System.out.println("[comet close ] id->" + connect.id());
    }

    @Override
    public CometResultSet onBeforeSendMessage(CometResultSet result, CometConnect connect) {
        return result;
    }
}
