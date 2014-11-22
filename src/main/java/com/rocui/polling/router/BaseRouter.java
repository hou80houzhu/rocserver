package com.rocui.polling.router;

import com.rocui.polling.base.ConnectManager.PollingConnect;
import com.rocui.polling.base.event.BaseEventHandler;
import com.rocui.polling.base.message.MessageWrapper;

public abstract class BaseRouter {

    public static String CARRIER_TYPE_CONNECT = "connect";
    public static String CARRIER_TYPE_GROUP = "group";
    public static String CARRIES_TYPE_BROADCAST = "broadcast";
    public static String CARRIES_TYPE_SELF = "self";
    public static String CARRIES_TYPE_SOME = "some";
    public static String CARRIES_TYPE_SOMEGROUP = "somegroup";
    public static String CARRIES_TYPE_WITHOUTSELF = "withoutself";
    public static String CARRIES_TYPE_WITHOUTGROUP = "withoutgroup";
    public static String CARRIES_TYPE_FILTER = "filter";

    public BaseEventHandler getEventHandler() {
        return null;
    }

    public abstract void rout(PollingConnect connect, MessageWrapper wrapper) throws Exception;
}
