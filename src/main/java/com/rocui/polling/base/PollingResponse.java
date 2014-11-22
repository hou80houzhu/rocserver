package com.rocui.polling.base;

import com.rocui.polling.base.message.MessageWrapper;
import com.rocui.polling.base.message.PollingMessage;
import com.rocui.polling.router.BaseRouter;
import java.util.ArrayList;
import java.util.List;

public class PollingResponse {

    private final List<MessageWrapper> list;

    public PollingResponse() {
        this.list = new ArrayList<>();
    }

    public void toBroadCast(PollingMessage message) {
        this.append(BaseRouter.CARRIES_TYPE_BROADCAST, "", message);
    }

    public void toSelf(PollingMessage message) {
        this.append(BaseRouter.CARRIES_TYPE_SELF, "", message);
    }

    public void toAllWithoutSelf(PollingMessage message) {
        this.append(BaseRouter.CARRIES_TYPE_WITHOUTSELF, "", message);
    }

    public void toAllWithoutGroup(String groupid, PollingMessage message) {
        this.append(BaseRouter.CARRIES_TYPE_WITHOUTGROUP, groupid, message);
    }

    public void toGroup(String to, PollingMessage message) {
        this.append(BaseRouter.CARRIER_TYPE_GROUP, to, message);
    }

    public void toGroups(List<String> tos, PollingMessage message) {
        this.append(BaseRouter.CARRIES_TYPE_SOMEGROUP, tos, message);
    }

    public void toConnect(String to, PollingMessage message) {
        this.append(BaseRouter.CARRIER_TYPE_CONNECT, to, message);
    }

    public void toConnects(List<String> tos, PollingMessage message) {
        this.append(BaseRouter.CARRIES_TYPE_SOME, tos, message);
    }

    public void toFilterConnect(ConnectFilter filter, PollingMessage message) {
        this.append(BaseRouter.CARRIES_TYPE_FILTER, filter, message);
    }

    public void append(String routerType, String to, PollingMessage message) {
        MessageWrapper carrier = new MessageWrapper(routerType, to, message);
        this.list.add(carrier);
    }

    public void append(String routerType, String to, String code, Object content, String callback) {
        MessageWrapper carrier = new MessageWrapper(routerType, to, PollingMessage.getMessage(code, content, callback));
        this.list.add(carrier);
    }

    public void append(String routerType, String to, Object content, String callback) {
        MessageWrapper carrier = new MessageWrapper(routerType, to, PollingMessage.getSuccessMessage(content, callback));
        this.list.add(carrier);
    }

    public void append(String routerType, List<String> tos, PollingMessage message) {
        MessageWrapper carrier = new MessageWrapper(routerType, tos, message);
        this.list.add(carrier);
    }

    public void append(String routerType, List<String> tos, String code, Object content, String callback) {
        MessageWrapper carrier = new MessageWrapper(routerType, tos, PollingMessage.getMessage(code, content, callback));
        this.list.add(carrier);
    }

    public void append(String routerType, List<String> tos, Object content, String callback) {
        MessageWrapper carrier = new MessageWrapper(routerType, tos, PollingMessage.getSuccessMessage(content, callback));
        this.list.add(carrier);
    }

    public void append(String routerType, ConnectFilter filter, PollingMessage message) {
        MessageWrapper carrier = new MessageWrapper(routerType, filter, message);
        this.list.add(carrier);
    }

    public void append(String routerType, ConnectFilter filter, Object content, String callback) {
        MessageWrapper carrier = new MessageWrapper(routerType, filter, PollingMessage.getSuccessMessage(content, callback));
        this.list.add(carrier);
    }

    public void clear() {
        this.list.clear();
    }

    public List<MessageWrapper> getMessageWrappers() {
        return this.list;
    }
}
