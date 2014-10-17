package com.rocui.comet.base.handler;

import com.rocui.comet.base.CometConnectCenter.CometConnect;
import com.rocui.comet.base.CometResultSet;
import java.util.List;

public abstract class CometBaseHandler {

    public abstract void onConnect(String id);

    public abstract void onClose(CometConnect connect);

    public abstract CometResultSet onBeforeSendMessage(CometResultSet result, CometConnect connect);
}
