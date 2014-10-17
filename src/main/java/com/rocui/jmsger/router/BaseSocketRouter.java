package com.rocui.jmsger.router;

import com.rocui.jmsger.base.connect.Connect;
import com.rocui.jmsger.base.connect.ConnectCollection;
import com.rocui.jmsger.base.result.ResultSet;
import com.rocui.jmsger.router.imp.GroupRouter;
import com.rocui.jmsger.router.imp.SimpleRouter;

public abstract class BaseSocketRouter {

    public static final String ROUT_BRODCAST = "broadcast";
    public static final String ROUT_TO = "to";
    public static final String ROUT_TOGROUP = "togroup";
    public static final String ROUT_SELF = "self";
    public static final String ROUT_WITHOUTSELF = "withoutself";

    private static final BaseSocketRouter simpleRouter = new SimpleRouter();
    private static final BaseSocketRouter groupRouter = new GroupRouter();

    public static final String ROUT_NAME_SIMPLE = "simplerout";
    public static final String ROUT_NAMW_GROUP = "grouprout";

    public static BaseSocketRouter getSocketRouter(String routName) {
        if (routName.equals(BaseSocketRouter.ROUT_NAMW_GROUP)) {
            return BaseSocketRouter.groupRouter;
        } else {
            return BaseSocketRouter.simpleRouter;
        }
    }

    public abstract void doRout(ResultSet resultset, Connect connect, ConnectCollection connections);
}
