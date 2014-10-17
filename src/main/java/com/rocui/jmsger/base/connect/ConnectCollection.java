package com.rocui.jmsger.base.connect;

import java.util.List;

public abstract class ConnectCollection {
    public abstract void broadCastMessage(String str);
    public abstract void sendToMessage(String id, String str);
    public abstract void sendMessageWidthOut(String id,String str);
    public abstract void sendToGroupMessage(List<String> ids, String str);
}
