package com.rocui.polling.base.event;

import com.rocui.polling.base.ConnectManager;
import com.rocui.polling.base.PollingResponse;
import javax.servlet.http.HttpServletRequest;

public class SessionGroupEventHandler extends BaseEventHandler {

    @Override
    public void onConnect(ConnectManager.PollingConnect connect, HttpServletRequest request, PollingResponse response) {
        String groupid = request.getSession().getId();
        connect.setGroupId(groupid);
    }

    @Override
    public void onClose(ConnectManager.PollingConnect connect, PollingResponse response) {
    }

}
